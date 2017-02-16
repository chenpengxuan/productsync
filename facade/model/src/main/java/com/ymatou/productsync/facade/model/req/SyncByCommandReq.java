package com.ymatou.productsync.facade.model.req;

import org.hibernate.validator.constraints.NotBlank;

import javax.ws.rs.*;

/**
 * Created by chenpengxuan on 2016/12/9.
 */
public class SyncByCommandReq extends BaseRequest {

    /**
     * 直播id
     */
    @QueryParam("ActivityId")
    private long activityId;

    /**
     * 商品id
     */
    @QueryParam("ProductId")
    private String productId;

    /**
     * 集成调用方app标识
     */
    @QueryParam("AppId")
    private String appId;

    /**
     * 业务场景指令
     */
    @NotBlank(message = "业务指令不能为空")
    @QueryParam("ActionType")
    private String actionType;

    /**
     * 业务凭据id
     */
    @QueryParam("TransactionId")
    private int transactionId;

    public long getActivityId() {
        return activityId;
    }

    public void setActivityId(long activityId) {
        this.activityId = activityId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }
}
