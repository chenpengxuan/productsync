/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.productsync.infrastructure.config;

import com.baidu.disconf.client.DisconfMgrBean;
import com.baidu.disconf.client.DisconfMgrBeanSecond;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/**
 * @author luoshiqian 2016/8/30 15:49
 */
@Configuration
public class DisconfMgr {

    @Bean(name = "disconfMgrBean", destroyMethod = "destroy")
    @DependsOn({"mongoProps", "tomcatConfig"})
    public DisconfMgrBean disconfMgrBean() {

        DisconfMgrBean disconfMgrBean = new DisconfMgrBean();
        disconfMgrBean.setScanPackage("com.ymatou.productsync");

        return disconfMgrBean;
    }

    @Bean(name = "disconfMgrBean2", destroyMethod = "destroy")
    public DisconfMgrBeanSecond disconfMgrBean2(DisconfMgrBean disconfMgrBean, TomcatConfig tomcatConfig) {
        DisconfMgrBeanSecond disconfMgrBeanSecond = new DisconfMgrBeanSecond();
        try {
            disconfMgrBeanSecond.init();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        Constants.TOMCAT_CONFIG = tomcatConfig;
        return disconfMgrBeanSecond;
    }

}
