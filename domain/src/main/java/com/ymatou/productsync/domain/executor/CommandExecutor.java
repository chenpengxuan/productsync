package com.ymatou.productsync.domain.executor;

import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.model.sql.TransactionInfo;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.infrastructure.config.props.BizProps;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.joda.time.DateTime;
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

    @Autowired
    private CommandQuery commandQuery;

    @Autowired
    private BizProps bizProps;

    /**
     * 更新业务凭据状态设置为参数异常无需重试
     *
     * @param transactionId 业务凭据id
     */
    public void updateTransactionInfo(int transactionId, SyncStatusEnum status){
        TransactionInfo transactionInfo = commandQuery.getTransactionInfo(transactionId);
        if(transactionInfo == null){
            logger.error("没有找到对应的业务凭据信息，transsactionId为{},",transactionId);
            return;
        }
        //针对是失败状态
        transactionInfo.setNewRetryTimes(
        transactionInfo.getNewTranStatus() == SyncStatusEnum.BizEXCEPTION.getCode()//业务异常
        || transactionInfo.getNewTranStatus() == SyncStatusEnum.FAILED.getCode()//系统异常
        ? transactionInfo.getNewRetryTimes() + 1:transactionInfo.getNewRetryTimes());
        transactionInfo.setNewTranStatus(status.getCode());
        transactionInfo.setNewUpdateTime(new DateTime().toString(Utils.DEFAULT_DATE_FORMAT));

        if(commandQuery.updateTransactionInfo(transactionInfo) <= 0){
            logger.error("更新商品业务凭据发生异常，transsactionId为{},",transactionId);
        }
    }

    /**
     * 获取待补单业务列表
     * @return
     */
    public List<TransactionInfo> getCompensationInfo(){
        return commandQuery.getCompensationInfo(bizProps.getReadCount(),bizProps.getTimeLimit(),bizProps.getRetryLimit());
    }

    /**
     * 检查业务凭据信息合法性
     * @param transactionId
     * @return
     */
    public boolean checkNeedProcessCommand(int transactionId){
        TransactionInfo transactionInfo = commandQuery.getTransactionInfo(transactionId);
        if(transactionInfo == null){
            logger.error("没有找到对应的业务凭据信息，transsactionId为{},",transactionId);
            return true;//如果没有找到对应的业务凭据信息，保证业务链路正常执行下去
        }
        return transactionInfo.getNewTranStatus() == SyncStatusEnum.INIT.getCode()
                ||transactionInfo.getNewTranStatus() == SyncStatusEnum.BizEXCEPTION.getCode()
                || transactionInfo.getNewTranStatus() == SyncStatusEnum.FAILED.getCode();
    }

    /**
     * 执行业务场景指令
     *
     * @param req
     * @param config
     */
    public boolean executeCommand(SyncByCommandReq req, ExecutorConfig config) throws IllegalArgumentException,BizException{
            updateTransactionInfo(req.getTransactionId(),SyncStatusEnum.PROCESSING);
            boolean isSuccess = mongoRepository.excuteMongo(config.loadSourceData(req.getActivityId(), req.getProductId()));
            updateTransactionInfo(req.getTransactionId(), isSuccess ? SyncStatusEnum.SUCCESS : SyncStatusEnum.FAILED);
            return isSuccess;
    }
}
