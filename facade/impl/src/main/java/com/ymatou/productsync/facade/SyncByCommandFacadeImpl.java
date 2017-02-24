package com.ymatou.productsync.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.domain.executor.*;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.model.sql.TransactionInfo;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.facade.model.resp.BaseResponse;
import com.ymatou.productsync.infrastructure.util.MessageBusDispatcher;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
        return executeCommand(req).isSuccess() ? "ok":"fail";
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
     * @param productId
     * @param snapshotVersion
     * @return
     */
    @GET
    @Path("/{cache:(?i:cache)}/{updateproductsnapshot:(?i:updateproductsnapshot)}")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    public BaseResponse updateProductSnapShot(@QueryParam("productId:(?i:productId)") String productId,@QueryParam("snapshotVersion:(?i:snapshotVersion)") String snapshotVersion){
        if (productId == null
                || productId.isEmpty()
                || snapshotVersion == null
                || snapshotVersion.isEmpty()) {
            DEFAULT_LOGGER.error("更新商品快照异常，异常原因为：参数不正确");
            BaseResponse response = BaseResponse.newSuccessInstance();
            response.setMessage("更新商品快照异常，异常原因为：参数不正确");
            return response;
        }
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String,Object>> updateData = new ArrayList<>();
        Map<String,Object> tempMap = new HashMap();
        tempMap.put("ver",snapshotVersion);
        tempMap.put("verupdate", new DateTime().toString(Utils.DEFAULT_DATE_FORMAT));
        updateData.add(tempMap);
        mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId),updateData));
        try{
        return mongoRepository.excuteMongo(mongoDataList) ? BaseResponse.newSuccessInstance():BaseResponse.newFailInstance(ErrorCode.BIZFAIL);
        }
        catch(Exception ex){
            DEFAULT_LOGGER.error("更新商品快照发生异常，异常原因为{}",ex.getMessage(),ex);
            return BaseResponse.newFailInstance(ErrorCode.FAIL);
        }
    }

    /**
     * 补单功能
     */
    @GET
    @Path("/{cache:(?i:cache)}/{compensateCommand:(?i:compensateCommand)}")
    @Override
    public void compensateCommand(){
        List<TransactionInfo> transactionInfoList = executor.getCompensationInfo();
        if(transactionInfoList != null && !transactionInfoList.isEmpty()){
            DEFAULT_LOGGER.info ("compensateCount is  "+transactionInfoList.size());
            List<SyncByCommandReq> syncByCommandReqList = transactionInfoList.parallelStream().map(x -> {
                SyncByCommandReq tempReq = new SyncByCommandReq();
                tempReq.setTransactionId(x.getTransactionId());
                tempReq.setActivityId(x.getLiveId());
                tempReq.setProductId(x.getProductId());
                tempReq.setActionType(x.getActionType());
                return tempReq;
            }).collect(Collectors.toList());
            CompletableFuture.runAsync(() ->
                    syncByCommandReqList.parallelStream().forEach(syncByCommandReq -> executeCommand(syncByCommandReq))
            );
        }else
        {
              DEFAULT_LOGGER.info ("compensateCount is 0 ");
        }
    }

    /**
     * 同步
     * @param req
     * @return
     */
    private BaseResponse executeCommand(SyncByCommandReq req) {
        ExecutorConfig config = executorConfigFactory.getCommand(req.getActionType());
        if (config == null) {
            executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.IllegalArgEXCEPTION);
            DEFAULT_LOGGER.info("发生业务指令异常，异常原因为：ProductId:{},LiveId:{},ActionType:{},TransactionId:{}", req.getProductId(), req.getActivityId(), req.getActionType(), req.getTransactionId());
            BaseResponse response = BaseResponse.newSuccessInstance();
            response.setMessage("没有对应场景，场景指令不正确");
            return response;
        }
        boolean syncSuccess = false;
        try {
            if(executor.checkNeedProcessCommand(req.getTransactionId())) {
                syncSuccess = executor.executeCommand(req, config);
                return syncSuccess ? BaseResponse.newSuccessInstance():BaseResponse.newFailInstance(ErrorCode.FAIL);
            }
        }catch (IllegalArgumentException argException) {
            executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.IllegalArgEXCEPTION);
            DEFAULT_LOGGER.info("发生业务参数级异常，异常原因为：ProductId:{},LiveId:{},ActionType:{},TransactionId:{},{}", req.getProductId(), req.getActivityId(), req.getActionType(), req.getTransactionId(), argException.getMessage());
            return BaseResponse.newSuccessInstance();
        } catch (BizException bizException) {
            executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.BizEXCEPTION);
            DEFAULT_LOGGER.error("发生业务级异常，异常原因为：ProductId:{},LiveId:{},ActionType:{},TransactionId:{},{}", req.getProductId(), req.getActivityId(), req.getActionType(), req.getTransactionId(), bizException.getMessage(),bizException);
            BaseResponse.newFailInstance(ErrorCode.BIZFAIL);
        }
        //执行成功的并且是商品相关操作
        if (syncSuccess && CmdTypeEnum.valueOf(req.getActionType()).ordinal() < CmdTypeEnum.AddActivity.ordinal()) {
            try {
                messageBusDispatcher.PublishAsync(req.getProductId(), req.getActionType());
                return BaseResponse.newSuccessInstance();
            } catch (MessageBusException e) {
                executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.SUCCESS);
                //目前商品部分业务相关指令消息的分发只是针对商品快照，如果发生消息总线异常，则只是记录到异常日志
                DEFAULT_LOGGER.error("同步服务发送消息发生异常,transactionId为{},productId为{},actionType为{}",
                        req.getTransactionId(), req.getProductId(), req.getActionType(), e);
                return BaseResponse.newSuccessInstance();
            } 
        }
        return BaseResponse.newFailInstance(ErrorCode.FAIL);
    }
}
