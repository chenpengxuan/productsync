package com.ymatou.productsync.domain;

/**
 * Created by zhangyifan on 2016/12/15.
 */
public enum SyncStatusEnum {

    /**
     * 参数异常无需重试
     */
    IllegalArgEXCEPTION(-2),

    /**
     * 失败
     */
    FAILED(-1),


    /**
     * 初始
     */
    INIT(0),


    /**
     * 成功
     */
    SUCCESS(1),

    ;


    private int code;

    private SyncStatusEnum(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
