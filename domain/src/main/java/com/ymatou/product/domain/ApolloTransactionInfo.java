package com.ymatou.product.domain;

/**
 * 业务日志表
 * Created by chenpengxuan on 2016/12/23.
 */
public class ApolloTransactionInfo {

    /**
     * 业务凭据id
     */
    private int realTransactionId;

    /**
     * 请求报文信息
     */
   private String tranMarkValue;

    public int getRealTransactionId() {
        return realTransactionId;
    }

    public void setRealTransactionId(int realTransactionId) {
        this.realTransactionId = realTransactionId;
    }

    public String getTranMarkValue() {
        return tranMarkValue;
    }

    public void setTranMarkValue(String tranMarkValue) {
        this.tranMarkValue = tranMarkValue;
    }
}
