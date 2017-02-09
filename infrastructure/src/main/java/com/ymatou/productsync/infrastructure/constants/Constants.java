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


    /**
     * Mongo商品库名
     */
    public static final String ProductDb = "Products";

    /**
     * 规格库名
     */
    public static final String CatalogDb = "Catalogs";

    /**
     * 活动商品库名
     */
    public static final String ActivityProductDb = "ActivityProducts";

    /**
     * 活动商品库名
     */
    public static final String ProductDescriptionDb = "ProductDescriptions";

    /**
     * Mongo直播库名
     */
    public static final String LiveDb = "Lives";

    /**
     * Mongo活动库名
     */
    public static final String ActivityDb = "Activity";

    /**
     * 活动模板库名
     */
    public static final String ActivityTemplateDb = "ActivityTemplate";

    /**
     * 直播商品库名
     */
    public static final String LiveProudctDb = "LiveProducts";
}
