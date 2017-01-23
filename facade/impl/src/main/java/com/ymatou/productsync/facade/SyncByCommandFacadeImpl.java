package com.ymatou.productsync.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.ExecutorConfigFactory;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.facade.model.resp.BaseResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.GET;
import javax.ws.rs.Path;

/**
 * 商品同步业务场景
 * 同时支持http rpc
 * Created by chenpengxuan on 2017/1/19.
 */
@Service(protocol =  { "rest", "dubbo" })
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
    @GET
    @Path("/{cache:(?i:cache)}/{invokemongocrud:(?i:invokemongocrud)}")
    @Override
    public BaseResponse syncByCommand(SyncByCommandReq req) {
        ExecutorConfig config = executorConfigFactory.getCommand(req.getActionType());
        if ( config == null ) {
            //参数错误，无需MQ重试
            executor.transactionIllegalArgEXCEPTION(req.getTransactionId());
            return BaseResponse.newSuccessInstance();
        }
//        executor.sync(req.getOrderId(), config, req.getCmdReqId());
        //// FIXME: 2017/1/20 这里添加同步执行器中的同步核心方法
        return BaseResponse.newSuccessInstance();
    }
}
