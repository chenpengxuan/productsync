package com.ymatou.productsync.domain.executor;

import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import org.apache.http.util.Asserts;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 业务指令执行器
 * Created by chenpengxuan on 2017/1/19.
 */
@Component
public class CommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);
    
    @Autowired
    private MongoRepository mongoRepository;

    /**
     * 更新业务凭据状态设置为参数异常无需重试
     *
     * @param transactionId 业务凭据id
     */
    public void updateTransactionInfo(int transactionId,SyncStatusEnum status) {
        ////// FIXME: 2017/2/6 添加操作业务凭据操作
    }

    /**
     * 执行业务场景指令
     * @param req
     * @param config
     */
    public boolean executorCommand(SyncByCommandReq req, ExecutorConfig config){
        try{
           boolean isSuccess = mongoRepository.excuteMongo(config.loadSourceData(req.getActivityId(),req.getProductId()));
            updateTransactionInfo(req.getTransactionId(),isSuccess ? SyncStatusEnum.SUCCESS:SyncStatusEnum.FAILED);
            Asserts.check(isSuccess,"");
            return isSuccess;
        }catch (IllegalArgumentException argExceptin){
            updateTransactionInfo(req.getTransactionId(),SyncStatusEnum.IllegalArgEXCEPTION);
        }catch (MessageBusException ex){
            updateTransactionInfo(req.getTransactionId(),SyncStatusEnum.FAILED);
        }
        return false;
    }
}
