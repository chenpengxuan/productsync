package com.ymatou.productsync.domain.executor;

import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

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
     * @param transactionId
     * @param mongoDataList
     */
    public void executorCommand(int transactionId, List<MongoData> mongoDataList){
        try{
           boolean isSuccess = mongoRepository.excuteMongo(mongoDataList);
            updateTransactionInfo(transactionId,isSuccess ? SyncStatusEnum.SUCCESS:SyncStatusEnum.FAILED);
        }catch (IllegalArgumentException argExceptin){
            updateTransactionInfo(transactionId,SyncStatusEnum.IllegalArgEXCEPTION);
        }catch (Exception ex){
            updateTransactionInfo(transactionId,SyncStatusEnum.FAILED);
        }
    }
}
