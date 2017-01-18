package com.ymatou.product.basemodel;

/**
 * Created by chenpengxuan on 2016/9/5.
 */
public class BaseResponse<S extends BaseResponse> extends BaseInfo {
    private static final long serialVersionUID = -4151184461517116847L;
    private boolean isSuccess;

    private BusinessCode code;

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

    public BusinessCode getErrorCode() {
        return code;
    }

    public void setBusinessCode(BusinessCode code) {
        this.code = code;
    }

    public String getBusinessCode() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public BaseResponse() {
    }

    public static BaseResponse newSuccessInstance( ) {
        BaseResponse result = new BaseResponse();
        result.setSuccess(true);
        result.setBusinessCode(BusinessCode.SUCESS);
        return result;
    }

    public static BaseResponse newFailInstance( BusinessCode errorCode ) {
        BaseResponse result = new BaseResponse();
        result.setSuccess(false);
        result.setBusinessCode(errorCode);
        result.setErrorMessage(errorCode == null ? "" : errorCode.getMessage());
        return result;
    }
}
