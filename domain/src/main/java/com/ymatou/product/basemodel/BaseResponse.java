package com.ymatou.product.basemodel;

/**
 * Created by chenpengxuan on 2016/9/5.
 */
public class BaseResponse<S extends BaseResponse> extends BaseInfo {
    private static final long serialVersionUID = -4151184461517116847L;
    private boolean isSuccess;

    private ErrorCode errorCode;

    private String errorMessage;

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public ErrorCode getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(ErrorCode errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public BaseResponse() {
    }

    public static BaseResponse newSuccessInstance( ) {
        BaseResponse result = new BaseResponse();
        result.setSuccess(true);
        return result;
    }

    public static BaseResponse newFailInstance( ErrorCode errorCode ) {
        BaseResponse result = new BaseResponse();
        result.setSuccess(false);
        result.setErrorCode(errorCode);
        result.setErrorMessage(errorCode == null ? "" : errorCode.getMessage());
        return result;
    }
}
