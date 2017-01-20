package com.ymatou.productsync.infrastructure.config.props;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

/**
 * Created by zhangyifan on 2016/12/20.
 */
@Component
@DisconfFile(fileName = "task.properties")
public class TaskProps {
    /**
     * 核心线程数，默认为1
     */
    private int corePoolSize;
    /**
     * 最大线程数，默认为Integer.MAX_VALUE
     */
    private int maxPoolSize;
    /**
     * 队列最大长度，一般需要设置值>=notifyScheduledMainExecutor.maxNum；默认为Integer.MAX_VALUE
     */
    private int queueCapacity;
    /**
     * 线程池维护线程所允许的空闲时间，默认为60s
     */
    private int keepAliveSeconds;


    @DisconfFileItem(name = "corePoolSize")
    public int getCorePoolSize() {
        return corePoolSize;
    }

    public void setCorePoolSize(int corePoolSize) {
        this.corePoolSize = corePoolSize;
    }

    @DisconfFileItem(name = "maxPoolSize")
    public int getMaxPoolSize() {
        return maxPoolSize;
    }

    public void setMaxPoolSize(int maxPoolSize) {
        this.maxPoolSize = maxPoolSize;
    }

    @DisconfFileItem(name = "queueCapacity")
    public int getQueueCapacity() {
        return queueCapacity;
    }

    public void setQueueCapacity(int queueCapacity) {
        this.queueCapacity = queueCapacity;
    }

    @DisconfFileItem(name = "keepAliveSeconds")
    public int getKeepAliveSeconds() {
        return keepAliveSeconds;
    }

    public void setKeepAliveSeconds(int keepAliveSeconds) {
        this.keepAliveSeconds = keepAliveSeconds;
    }

}
