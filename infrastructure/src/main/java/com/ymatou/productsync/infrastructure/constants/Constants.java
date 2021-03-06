package com.ymatou.productsync.infrastructure.constants;


import com.ymatou.productsync.infrastructure.config.TomcatConfig;

/**
 * Created by zhangyifan on 2016/12/9.
 */
public class Constants {
    public static final String APP_ID = "productsync.iapi.ymatou.com";
    public static final String LOG_PREFIX = "logPrefix";
    public static TomcatConfig TOMCAT_CONFIG;
    public static final String SNAPSHOP_MQ_ID = "product";
    public static final String SNAPSHOP_MQ_CODE = "snapshotmq_from_apollo";
    public static final String[] IGNORE_COMMANDTYPE = new String[]{"ActivityStockChange","ActivityStockTransfer","ModifyActivityPrice","UpdateProductsInfo"};

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
     * 商品图文描述（2）
     */
    public static  final  String ProductDescExtraDb="ProductDescExtra";

    /**
     * Mongo直播库名
     */
    public static final String LiveDb = "Lives";

    /**
     * 直播商品库名
     */
    public static final String LiveProudctDb = "LiveProducts";

    /**
     * 商品相关时间戳表
     */
    public static final String ProductTimeStamp = "ProductTimeStamp";
}
