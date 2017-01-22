package com.ymatou.productsync.domain;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

/**
 * 同步执行器
 * Created by chenpengxuan on 2017/1/19.
 */
@Component
public class CommandExecutor {
    private static final Logger logger = LoggerFactory.getLogger(CommandExecutor.class);

    /**
     * 更新业务凭据状态设置为参数异常无需重试
     *
     * @param transactionId 业务凭据id
     */
    public void transactionIllegalArgEXCEPTION(int transactionId) {
        updateTransactionInfo(transactionId,SyncStatusEnum.IllegalArgEXCEPTION);
    }
    
    private void updateTransactionInfo(int transactionId,SyncStatusEnum status){
        //// FIXME: 2017/1/19 更新业务凭据信息1
    }
}
