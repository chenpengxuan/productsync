package com.ymatou.productsync.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.ExecutorConfigFactory;
import com.ymatou.productsync.domain.executor.SyncStatusEnum;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.facade.model.resp.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * 商品同步业务场景
 * 同时支持http rpc
 * Created by chenpengxuan on 2017/1/19.
 */
@Service(protocol =  { "rest","dubbo"})
@Component
@Path("/{api:(?i:api)}")
public class SyncByCommandFacadeImpl implements SyncCommandFacade{
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

    /**
     * 根据业务场景指令同步相关信息
     * @param req 基于业务场景的请求
     * @return
     */
    @POST
    @Path("/{cache:(?i:cache)}/{invokemongocrud:(?i:invokemongocrud)}")
    @Override
    @Produces(MediaType.APPLICATION_JSON)
    @Consumes(MediaType.APPLICATION_JSON)
    public BaseResponse syncByCommand(SyncByCommandReq req) {
        ExecutorConfig config = executorConfigFactory.getCommand(req.getActionType());
        if ( config == null ) {
            //参数错误，无需MQ重试
            executor.updateTransactionInfo(req.getTransactionId(), SyncStatusEnum.IllegalArgEXCEPTION);
            return BaseResponse.newSuccessInstance();
        }
        try {
            executor.executorCommand(req.getTransactionId(), config.loadSourceData(req.getActivityId(), req.getProductId()));
        }catch (MessageBusException e){
            ////// FIXME: 2017/2/9 mq 异常处理
        }
        return BaseResponse.newSuccessInstance();
    }
}
