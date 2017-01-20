package com.ymatou.productsync.infrastructure.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

/**
 * Created by zhangyifan on 2016/12/22.
 */
@Configuration
public class SchedulerConfig {

    @Bean
    public SchedulerFactoryBean scheduler() {
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();
        //TODO 需要处理
        //schedulerFactoryBean.setConfigLocation(new ClassPathResource("quartz.properties"));
        return schedulerFactoryBean;
    }
}
