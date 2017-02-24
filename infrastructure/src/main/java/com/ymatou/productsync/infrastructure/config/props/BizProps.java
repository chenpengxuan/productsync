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
    private int timeLimit;

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

    @DisconfFileItem(name = "timeLimit")
    public int getTimeLimit() {
        return timeLimit;
    }

    public void setTimeLimit(int timeLimit) {
        this.timeLimit = timeLimit;
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

    public void setBizExceptionWarning(boolean bizExceptionWarning) {
        this.bizExceptionWarning = bizExceptionWarning;
    }
}
