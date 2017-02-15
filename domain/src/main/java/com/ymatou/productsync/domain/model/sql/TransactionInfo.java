package com.ymatou.productsync.domain.model.sql;

/**
 * 业务凭据信息
 * Created by chenpengxuan on 2017/2/13.
 */
public class TransactionInfo {
    /**
     * 业务凭据id
     */
    private int transactionId;

    /**
     * 直播id
     */
    private int liveId;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 业务指令
     */
    private String actionType;

    /**
     * 业务执行状态
     */
    private int newTranStatus;

    /**
     * 业务重试次数
     */
    private int newRetryTimes;

    /**
     * 业务操作更新时间
     */
    private String newUpdateTime;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getNewTranStatus() {
        return newTranStatus;
    }

    public void setNewTranStatus(int newTranStatus) {
        this.newTranStatus = newTranStatus;
    }

    public int getNewRetryTimes() {
        return newRetryTimes;
    }

    public void setNewRetryTimes(int newRetryTimes) {
        this.newRetryTimes = newRetryTimes;
    }

    public String getNewUpdateTime() {
        return newUpdateTime;
    }

    public void setNewUpdateTime(String newUpdateTime) {
        this.newUpdateTime = newUpdateTime;
    }
}
