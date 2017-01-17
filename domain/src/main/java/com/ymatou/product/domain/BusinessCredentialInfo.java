package com.ymatou.product.domain;

import java.util.Date;

/**
 * 业务凭据信息
 * Created by chenpengxuan on 2017/1/16.
 */
public class BusinessCredentialInfo {
    /**
     * 事务id
     */
    private int transactionId;

    /**
     * 业务场景
     */
    private String actionType;

    /**
     * 商品id
     */
    private String productId;

    /**
     * 直播id
     */
    private int liveId;

    /**
     * 业务执行状态 0表示初始化 1表示执行中 2表示执行成功 3表示执行失败
     */
    private int tranStatus;

    /**
     * 重试次数
     */
    private int retryTimes;

    /**
     * 创建日期
     */
    private Date createTime;

    /**
     * 更新日期
     */
    private Date updateTime;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public int getTranStatus() {
        return tranStatus;
    }

    public void setTranStatus(int tranStatus) {
        this.tranStatus = tranStatus;
    }

    public int getRetryTimes() {
        return retryTimes;
    }

    public void setRetryTimes(int retryTimes) {
        this.retryTimes = retryTimes;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }
}
