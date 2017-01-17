package com.ymatou.product.basemodel;

/**
 * Created by chenpengxuan on 2016/9/5.
 */
public class ApiException extends Exception {
    private static final long serialVersionUID = 2021661203948777920L;

    /**
     * 业务检查异常编码
     */
    private ErrorCode errorCode;

    /**
     * 业务检查异常信息
     */
    private String errorMsg;

    public String getErrorMsg() {
        return errorMsg;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public ApiException(ErrorCode errorCode, String errorMsg)
    {
        super(errorCode + ":" + errorMsg);
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }
}
