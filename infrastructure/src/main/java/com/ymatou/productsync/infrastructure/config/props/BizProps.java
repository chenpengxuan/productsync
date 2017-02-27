package com.ymatou.productsync.infrastructure.config.props;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

/**
 * 业务相关配置
 * Created by zhangyifan on 2016/12/21.
 */
@Component
@DisconfFile(fileName = "biz.properties")
public class BizProps {
    /**
     * 补单读取条目数限制
     */
    private int readCount;

    /**
     * 补单初始状态重试时间限制
     */
    private int minuteLimit;

    /**
     * 补单创建时间范围限制
     */
    private int hourLimit;

    /**
     * 重试次数限制
     */
    private int retryLimit;

    /**
     * 业务异常告警开关
     */
    private boolean bizExceptionWarning;

    @DisconfFileItem(name = "readCount")
    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }

    @DisconfFileItem(name = "retryLimit")
    public int getRetryLimit() {
        return retryLimit;
    }

    public void setRetryLimit(int retryLimit) {
        this.retryLimit = retryLimit;
    }

    @DisconfFileItem(name = "bizExceptionWarning")
    public boolean isBizExceptionWarning() {
        return bizExceptionWarning;
    }

    @DisconfFileItem(name = "minuteLimit")
    public int getMinuteLimit() {
        return minuteLimit;
    }

    public void setMinuteLimit(int minuteLimit) {
        this.minuteLimit = minuteLimit;
    }

    @DisconfFileItem(name = "hourLimit")
    public int getHourLimit() {
        return hourLimit;
    }

    public void setHourLimit(int hourLimit) {
        this.hourLimit = hourLimit;
    }

    public void setBizExceptionWarning(boolean bizExceptionWarning) {
        this.bizExceptionWarning = bizExceptionWarning;
    }
}
