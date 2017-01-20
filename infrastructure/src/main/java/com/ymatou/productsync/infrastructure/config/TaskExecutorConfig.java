package com.ymatou.productsync.infrastructure.config;


import com.ymatou.productsync.infrastructure.config.props.TaskProps;
import com.ymatou.productsync.infrastructure.util.LogRejectedPolicy;
import com.ymatou.productsync.infrastructure.util.ThreadPoolContextTaskExecutor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskExecutor;

/**
 * Created by zhangyifan on 2016/12/19.
 */
@Configuration
public class TaskExecutorConfig {

    @Autowired
    private TaskProps taskProps;

    @Bean
    public TaskExecutor taskExecutor() {
        ThreadPoolContextTaskExecutor taskExecutor = new ThreadPoolContextTaskExecutor();
        taskExecutor.setCorePoolSize(taskProps.getCorePoolSize());
        taskExecutor.setMaxPoolSize(taskProps.getMaxPoolSize());
        taskExecutor.setQueueCapacity(taskProps.getQueueCapacity());
        taskExecutor.setKeepAliveSeconds(taskProps.getKeepAliveSeconds());
        taskExecutor.setRejectedExecutionHandler(new LogRejectedPolicy());
        return taskExecutor;
    }
}
