package com.ymatou.productsync.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.performancemonitorclient.PerformanceStatisticContainer;
import com.ymatou.productsync.domain.executor.*;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.model.sql.TransactionInfo;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.facade.model.resp.BaseResponse;
import com.ymatou.productsync.infrastructure.config.props.BizProps;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.LogWrapper;
import com.ymatou.productsync.infrastructure.util.MessageBusDispatcher;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.*;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

/**
 * 商品同步业务场景
 * 同时支持http rpc
 * Created by chenpengxuan on 2017/1/19.
 */
@Service(protocol = {"rest", "dubbo"})
@Component
@Path("/{api:(?i:api)}")
public class SyncByCommandFacadeImpl implements SyncCommandFacade {

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(SyncByCommandFacadeImpl.class);

    @Autowired
    private LogWrapper logWrapper;

    /**
     * 业务指令器工厂
     */
    @Autowired
    private ExecutorConfigFactory executorConfigFactory;

    /**
     * 同步执行器
     */
    @Autowired
    private CommandExecutor executor;

    @Autowired
    private MongoRepository mongoRepository;

    /**
     * 消息总线客户端
     */
    @Autowired
    private MessageBusDispatcher messageBusDispatcher;

    @Autowired
    private BizProps bizProps;

    @Override
    @GET
    @Path("/{warmup:(?i:warmup)}")
    @Produces({MediaType.TEXT_PLAIN})
    public String warmUp() {
        return "ok";
    }

    /**
     * 根据业务场景指令同步相关信息
     *
     * @param req 基于业务场景的请求
     * @return
     */
    @POST
    @Path("/{cache:(?i:cache)}/{invokemongocrud:(?i:invokemongocrud)}")
    @Override
    @Consumes(MediaType.APPLICATION_JSON)
    public String syncByCommand(SyncByCommandReq req) {
        BaseResponse response = executeCommand(req);
        return response.isSuccess() ? "ok" : Utils.toJSONString(response);
    }

    /**
     * Get方式供直连调用
     *
     * @param req 基于业务场景的请求
     * @return
     */
    @GET
    @Path("/{cache:(?i:cache)}/{mongocrud:(?i:mongocrud)}")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse updateByCommandSync(@BeanParam SyncByCommandReq req) {
        return executeCommand(req);
    }

    /**
     * 更新商品快照信息
     *
     * @param productId
     * @param snapshotVersion
     * @return
     */
    @GET
    @Path("/{cache:(?i:cache)}/{updateproductsnapshot:(?i:updateproductsnapshot)}")
    @Produces(MediaType.APPLICATION_JSON)
    @Override
    public BaseResponse updateProductSnapShot(@QueryParam("productId") String productId, @QueryParam("snapshotVersion") String snapshotVersion) {
        if (productId == null
                || productId.isEmpty()
                || snapshotVersion == null
                || snapshotVersion.isEmpty()) {
            logWrapper.recordErrorLog("更新商品快照异常，异常原因为：参数不正确");
            BaseResponse response = BaseResponse.newSuccessInstance();
            response.setMessage("更新商品快照异常，异常原因为：参数不正确");
            return response;
        }
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> updateData = new ArrayList<>();
        Map<String, Object> tempMap = new HashMap();
        tempMap.put("ver", snapshotVersion);
        tempMap.put("verupdate", Utils.getNow());
        updateData.add(tempMap);
        mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), updateData));
        try {
            return mongoRepository.excuteMongo(mongoDataList) ? BaseResponse.newSuccessInstance() : BaseResponse.newFailInstance(SyncStatusEnum.FAILED.getCode(), "操作mongo失败");
        } catch (Exception ex) {
            logWrapper.recordErrorLog("更新商品快照发生异常，异常原因为{}", ex.getMessage(), ex);
            return BaseResponse.newFailInstance(SyncStatusEnum.FAILED.getCode(), ex.getMessage());
        }
    }

    /**
     * 补单功能
     */
    @POST
    @Path("/{cache:(?i:cache)}/{compensatecommand:(?i:compensatecommand)}")
    @Override
    public String compensateCommand() {
        try {
            List<TransactionInfo> transactionInfoList = executor.getCompensationInfo();
            if (transactionInfoList != null && !transactionInfoList.isEmpty()) {
                DEFAULT_LOGGER.info("compensateCount is  " + transactionInfoList.size());
                List<SyncByCommandReq> syncByCommandReqList = transactionInfoList.stream().map(x -> {
                    SyncByCommandReq tempReq = new SyncByCommandReq();
                    tempReq.setTransactionId(x.getTransactionId());
                    tempReq.setActivityId(x.getLiveId());
                    tempReq.setProductId(x.getProductId());
                    tempReq.setActionType(x.getActionType());
                    return tempReq;
                }).collect(Collectors.toList());
                CompletableFuture.runAsync(() ->
                        syncByCommandReqList.stream().forEach(syncByCommandReq -> {
                            executor.updateTransactionInfo(syncByCommandReq.getTransactionId());
                            executeCommand(syncByCommandReq);
                        })
                );
            } else {
                DEFAULT_LOGGER.info("compensateCount is 0 ");
            }
        } catch (Exception ex) {
            logWrapper.recordErrorLog("补单发生异常", ex);
            return ex != null ? Utils.toJSONString(ex) : "fail,发生异常，且异常对象ex为空";
        }
        return "ok";
    }

    /**
     * 同步
     *
     * @param req
     * @return
     */
    private BaseResponse executeCommand(SyncByCommandReq req) {
        //针对忽略的业务指令场景，直接返回不做任何处理
        if(checkIgnoreCommandType(req.getActionType())){
            return BaseResponse.newSuccessInstance();
        }
        ExecutorConfig config = executorConfigFactory.getCommand(req.getActionType());
        //增加定制化性能监控汇报
        BaseResponse result = PerformanceStatisticContainer.addWithReturn(() -> {
            if (config == null) {
                executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.IllegalArgEXCEPTION);
                DEFAULT_LOGGER.info("发生业务指令异常，异常原因为：ProductId:{},LiveId:{},ActionType:{},TransactionId:{}", req.getProductId(), req.getActivityId(), req.getActionType(), req.getTransactionId());
                BaseResponse response = BaseResponse.newSuccessInstance();
                response.setMessage("没有对应场景，场景指令不正确");
                return response;
            }
            boolean syncSuccess = false;
            try {
                if (executor.checkNeedProcessCommand(req.getTransactionId())) {
                    syncSuccess = executor.executeCommand(req, config);
                    return syncSuccess ? BaseResponse.newSuccessInstance() : BaseResponse.newFailInstance(SyncStatusEnum.FAILED.getCode(), "同步失败");
                }
            } catch (IllegalArgumentException argException) {
                executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.IllegalArgEXCEPTION);
                DEFAULT_LOGGER.info("发生业务参数级异常，异常原因为：ProductId:{},LiveId:{},ActionType:{},TransactionId:{},{}", req.getProductId(), req.getActivityId(), req.getActionType(), req.getTransactionId(), argException.getMessage());
                return BaseResponse.newSuccessInstance();
            } catch (BizException bizException) {
                executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.BizEXCEPTION);
                logWrapper.recordErrorLog("发生业务级异常，异常原因为：ProductId:{},LiveId:{},ActionType:{},TransactionId:{},{}", req.getProductId(), req.getActivityId(), req.getActionType(), req.getTransactionId(), bizException.getMessage(), bizException);
                return BaseResponse.newFailInstance(SyncStatusEnum.BizEXCEPTION.getCode(), "发生业务级异常:" + bizException.getMessage());
            }catch (Exception ex){
                executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.FAILED);
                logWrapper.recordErrorLog("发生系统级异常，异常原因为：ProductId:{},LiveId:{},ActionType:{},TransactionId:{},{}", req.getProductId(), req.getActivityId(), req.getActionType(), req.getTransactionId(), ex.getMessage(), ex);
                return BaseResponse.newFailInstance(SyncStatusEnum.FAILED.getCode(), "系统异常");
            }
            //执行成功的并且是商品相关操作
            if (syncSuccess && CmdTypeEnum.valueOf(req.getActionType()).ordinal() < CmdTypeEnum.AddActivity.ordinal()) {
                try {
                    messageBusDispatcher.publishAsync(req.getProductId(), req.getActionType());
                    return BaseResponse.newSuccessInstance();
                } catch (MessageBusException e) {
                    //目前商品部分业务相关指令消息的分发只是针对商品快照，如果发生消息总线异常，则只是记录到异常日志
                    logWrapper.recordErrorLog("同步服务发送消息发生异常,transactionId为{},productId为{},actionType为{}",
                            req.getTransactionId(), req.getProductId(), req.getActionType(), e);
                    return BaseResponse.newSuccessInstance();
                }
            }
            return BaseResponse.newFailInstance(SyncStatusEnum.FAILED.getCode(), "系统异常");
        }, "QuerySqlData_" + (config != null ? config.getCommand().name() : ""), Constants.APP_ID);
        return result;
    }

    /**
     * 业务指令白名单
     * @param actionType
     * @return true表示可以忽略 false表示不能忽略 针对不合法的请求返回不能忽略，后继逻辑处理
     */
    private boolean checkIgnoreCommandType(String actionType){
        if(actionType == null || actionType.isEmpty()){
            return false;
        }
        List<String> ignoreCommandTypeList = Arrays.asList(Constants.IGNORE_COMMANDTYPE);
        return ignoreCommandTypeList.contains(actionType);
    }
}
