/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 * 
 * All rights reserved.
 */
package com.ymatou.product.basemodel;

/**
 *
 * 
 * @author tuwenjie
 *
 */
public enum ErrorCode {

    // 请求参数非法
    ILLEGAL_ARGUMENT(100, "请求参数非法"),

    // 请求处理过程中，出现未知错误
    UNKNOWN(199, "未知错误，系统异常"),

    ;

    private int code;

    private String message;

    private ErrorCode(int code, String message) {
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
    public static ErrorCode getByCode(int code) {
        for (ErrorCode errorCode : ErrorCode.values()) {
            if (errorCode.getCode() == code) {
                return errorCode;
            }
        }
        return null;
    }
}
