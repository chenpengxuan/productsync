package com.ymatou.productsync.domain.executor;

import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.model.sql.TransactionInfo;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.infrastructure.config.props.BizProps;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.LogWrapper;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.joda.time.DateTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

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

    @Autowired
    private LogWrapper logWrapper;

    private static final String[] recordChangeTableArray = {Constants.ProductDb,
            Constants.CatalogDb,
            Constants.ActivityProductDb,
            Constants.LiveProudctDb};

    /**
     * 更新业务凭据状态设置
     *
     * @param transactionId 业务凭据id
     */
    public void updateTransactionInfo(int transactionId, SyncStatusEnum status) {
        try {
            TransactionInfo transactionInfo = commandQuery.getTransactionInfo(transactionId);
            if (transactionInfo == null) {
                logWrapper.recordErrorLog("没有找到对应的业务凭据信息，transactionId为{},", transactionId);
                return;
            }
            transactionInfo.setNewTranStatus(status.getCode());
            transactionInfo.setNewUpdateTime(new DateTime().toString(Utils.DEFAULT_DATE_FORMAT));

            if (commandQuery.updateTransactionInfo(transactionInfo) <= 0) {
                logWrapper.recordErrorLog("更新商品业务凭据发生异常，transactionId为{},", transactionId);
            }
        } catch (Exception ex) {
            logWrapper.recordErrorLog("更新商品业务凭据发生异常，transactionId为{},", transactionId, ex);
        }
    }

    /**
     * 设置重试次数
     *
     * @param transactionId
     */
    public void updateTransactionInfo(int transactionId) {
        try {
            TransactionInfo transactionInfo = commandQuery.getTransactionInfo(transactionId);
            if (transactionInfo == null) {
                logWrapper.recordErrorLog("没有找到对应的业务凭据信息，transactionId为{},", transactionId);
                return;
            }
            transactionInfo.setNewRetryTimes(transactionInfo.getNewRetryTimes() + 1);

            if (commandQuery.updateTransactionInfo(transactionInfo) <= 0) {
                logWrapper.recordErrorLog("更新商品业务凭据发生异常，transactionId为{},", transactionId);
            }
        } catch (Exception ex) {
            logWrapper.recordErrorLog("更新商品业务凭据发生异常，transactionId为{},", transactionId, ex);
        }
    }

    /**
     * 获取待补单业务列表
     *
     * @return
     */
    public List<TransactionInfo> getCompensationInfo() {
        return commandQuery.getCompensationInfo(bizProps.getReadCount(), bizProps.getMinuteLimit(), bizProps.getHourLimit(), bizProps.getRetryLimit());
    }

    /**
     * 检查业务凭据信息合法性
     *
     * @param transactionId
     * @return
     */
    public boolean checkNeedProcessCommand(int transactionId) {
        try {
            TransactionInfo transactionInfo = commandQuery.getTransactionInfo(transactionId);
            if (transactionInfo == null) {
                logWrapper.recordErrorLog("没有找到对应的业务凭据信息，transactionId为{},", transactionId);
                return true;//如果没有找到对应的业务凭据信息，保证业务链路正常执行下去
            }
            return transactionInfo.getNewTranStatus() == SyncStatusEnum.INIT.getCode()
                    || transactionInfo.getNewTranStatus() == SyncStatusEnum.BizEXCEPTION.getCode()
                    || transactionInfo.getNewTranStatus() == SyncStatusEnum.FAILED.getCode();
        } catch (Exception ex) {
            logWrapper.recordErrorLog("查询业务凭据发生异常,transactionId为{},", transactionId, ex);
            return true;
        }
    }

    /**
     * 执行业务场景指令
     *
     * @param req
     * @param config
     */
    public boolean executeCommand(SyncByCommandReq req, ExecutorConfig config) throws
            IllegalArgumentException,
            BizException {
        boolean isSuccess = mongoRepository.excuteMongo(config.loadSourceData(req.getActivityId(), req.getProductId()));
        updateTransactionInfo(req.getTransactionId(), isSuccess ? SyncStatusEnum.SUCCESS : SyncStatusEnum.FAILED);
        return isSuccess;
    }

    /**
     * 同步更新时间戳
     * 目前只更新商品相关
     *
     * @param mongoDataList
     * @return
     */
    public boolean syncProductChangeRange(SyncByCommandReq req,List<MongoData> mongoDataList) {
        if(mongoDataList == null){
            logWrapper.recordInfoLog("当前场景数据变更为空,对应场景为{}",req.getActionType());
            throw new IllegalArgumentException("当前场景数据变更为空,对应场景为:" + req.getActionType());
        }
        ProductChangedRange productChangedRange = new ProductChangedRange();
        List<String> productIdList = mongoDataList
                .stream()
                .map(x -> Optional.ofNullable((String)x.getMatchCondition().get("spid")).orElse(""))
                .collect(Collectors.toList());
        productIdList.add(req.getProductId());

        productIdList = productIdList
                .stream()
                .distinct()
                .collect(Collectors.toList());

        productChangedRange.setProductIdList(productIdList);

        List<String> productTableNameList = mongoDataList
                .stream()
                .filter(z -> Arrays.asList(recordChangeTableArray).contains(z.getTableName()))
                .map(x -> x.getTableName())
                .collect(Collectors.toList());

        return
    }
}
