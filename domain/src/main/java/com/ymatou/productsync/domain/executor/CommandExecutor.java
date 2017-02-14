package com.ymatou.productsync.domain.executor;

import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.TransactionInfo;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.apache.http.util.Asserts;
import org.joda.time.DateTime;
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

    @Autowired
    private CommandQuery commandQuery;

    /**
     * 更新业务凭据状态设置为参数异常无需重试
     *
     * @param transactionId 业务凭据id
     */
    public void updateTransactionInfo(int transactionId, SyncStatusEnum status) {
        TransactionInfo transactionInfo = commandQuery.getTransactionInfo(transactionId);
        if(transactionInfo == null){
            logger.error(String.format("没有找到对应的业务凭据信息，transsactionId为%d,",transactionId));
        }
        transactionInfo.setNewRetryTimes(
        transactionInfo.getNewTranStatus() != SyncStatusEnum.BizEXCEPTION.ordinal()
        && transactionInfo.getNewTranStatus() != SyncStatusEnum.FAILED.ordinal()
        ? transactionInfo.getNewRetryTimes():transactionInfo.getNewRetryTimes() + 1);
        transactionInfo.setNewTranStatus(status.ordinal());
        transactionInfo.setNewUpdateTime(new DateTime().toString(Utils.DEFAULT_DATE_FORMAT));
        if(commandQuery.updateTransactionInfo(transactionInfo) <= 0){
            logger.error(String.format("更新商品业务凭据发生异常，transsactionId为%d,",transactionId));
        }
    }

    /**
     * 执行业务场景指令
     *
     * @param req
     * @param config
     */
    public boolean executorCommand(SyncByCommandReq req, ExecutorConfig config) throws IllegalArgumentException,BizException {
            boolean isSuccess = mongoRepository.excuteMongo(config.loadSourceData(req.getActivityId(), req.getProductId()));
            updateTransactionInfo(req.getTransactionId(), isSuccess ? SyncStatusEnum.SUCCESS : SyncStatusEnum.FAILED);
            return isSuccess;
    }
}
