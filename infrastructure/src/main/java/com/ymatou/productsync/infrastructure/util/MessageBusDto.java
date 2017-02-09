package com.ymatou.productsync.infrastructure.util;

/**
 * Created by chenfei on 2017/2/9.
 */
public class MessageBusDto {

    public String getAppId() {
        return appId;
    }

    public void setAppId(String appId) {
        this.appId = appId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    private String appId;

    private  String productId;

    private  String actionType;
}
