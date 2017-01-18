/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 * 
 * All rights reserved.
 */
package com.ymatou.product.basemodel;

/**
 * 业务编码
 * Created by chenpengxuan on 2016/9/5.
 */
public enum BusinessCode {

    // 请求参数非法
    ILLEGAL_ARGUMENT(100, "请求参数非法"),

    // 请求处理过程中，出现未知错误
    UNKNOWN(199, "未知错误，系统异常"),

    //请求处理成功
    SUCESS(200,"操作成功"),
    ;

    private int code;

    private String message;

    private BusinessCode(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public int getCode() {
        return code;
    }

    public String getMessage() {
        return message;
    }

    /**
     * 通过错误码获取枚举项
     * 
     * @param code
     * @return
     */
    public static BusinessCode getByCode(int code) {
        for (BusinessCode bsCode : BusinessCode.values()) {
            if (bsCode.getCode() == code) {
                return bsCode;
            }
        }
        return null;
    }
}
