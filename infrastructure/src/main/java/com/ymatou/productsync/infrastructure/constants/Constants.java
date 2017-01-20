package com.ymatou.productsync.infrastructure.constants;


import com.ymatou.productsync.infrastructure.config.TomcatConfig;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * Created by zhangyifan on 2016/12/9.
 */
public class Constants {
    public static final String APP_ID = "productsync.iapi.ymatou.com";
    public static final String LOG_PREFIX = "logPrefix";
    public static ConfigurableApplicationContext ctx;
    public static TomcatConfig TOMCAT_CONFIG;
}
