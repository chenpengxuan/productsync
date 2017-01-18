/**
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *
 * All rights reserved.
 */
package com.ymatou.product.basemodel;

/**
 * 与.NET http客户端适配的响应
 * 
 * @author tuwenjie 2016年5月17日 下午3:08:20
 *
 */
public class BaseNetCompatibleResp extends BaseInfo {

    private static final long serialVersionUID = 256845177242093093L;

    // http响应码
    private int code;

    private int bcode;

    // 业务错误码描述
    private String msg;

    // 业务报文体
    private BaseResponse data;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }


    public int getBcode() {
        return bcode;
    }

    public void setBcode(int bcode) {
        this.bcode = bcode;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public BaseResponse getData() {
        return data;
    }

    public void setData(BaseResponse data) {
        this.data = data;
    }

    public static BaseNetCompatibleResp newInstance(BaseResponse baseResp) {
        assert baseResp != null;
        BaseNetCompatibleResp restResp = new BaseNetCompatibleResp();
        restResp.setData(baseResp);
        restResp.setMsg(baseResp.getErrorMessage());
        if (baseResp.isSuccess()) {
            restResp.setCode(200); // 成功
        } else {
            BusinessCode errorCode = baseResp.getErrorCode()== null ? BusinessCode.UNKNOWN : baseResp.getErrorCode();
            restResp.setBcode(errorCode.getCode());
            switch (errorCode) {
                case ILLEGAL_ARGUMENT:
                    restResp.setCode(400); // 参数错误
                    break;
                case UNKNOWN:
                    restResp.setCode(500); // 未知异常
                    break;
                default:
                    restResp.setCode(201); // 业务因明确原因处理失败
            }
        }
        return restResp;
    }

}
