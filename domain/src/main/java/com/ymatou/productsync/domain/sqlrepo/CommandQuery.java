package com.ymatou.productsync.domain.sqlrepo;

import com.ymatou.productsync.domain.model.sql.TransactionInfo;
import com.ymatou.productsync.infrastructure.config.datasource.TargetDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * sql数据查询相关
 * Created by chenpengxuan on 2017/1/22.
 */
public interface CommandQuery {
    /**
     * 商品设置、取消橱窗商品-cf
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getLiveProductTop(@Param("productId") String productId, @Param("activityId") long activityId);

    /**
     * 根据直播id获取直播基本信息
     *
     * @param activityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getLiveProductByActivityId(@Param("activityId") long activityId);

    /**
     * 根据直播id获取商品新品信息
     *
     * @param activityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductNewTimeByActivityId(@Param("activityId") long activityId);

    /**
     * 根据商品id获取商品详情
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductDetailInfo(@Param("productId") String productId);

    /**
     * 根据商品id获取商品规格详情
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductCatalogInfo(@Param("productId") String productId);

    /**
     * 根据商品id获取商品描述
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductDescInfo(@Param("productId") String productId);

    /**
     * 根据商品编号获取有效的商品主图列表
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductPictureList(@Param("productId") String productId);

    /**
     * 获取待删除的商品-cf
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getDeleteProducts(@Param("productId") String productId);


    /**
     * 获取直播商品时间-cf
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getLiveProductTime(@Param("productId") String productId, @Param("activityId") long activityId);

    /**
     * 获取商品的品牌品类信息
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductBrandAndCategory(@Param("productId") String productId);

    /**
     * 获取有效的和即将开始的Ymt_ProductsInLive
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getValidLiveByProductId(@Param("productId") String productId);

    /**
     * 根据activytid获取已移除商品列表
     *
     * @param ActivityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductInfoByActivityIdWithDeleted(@Param("ActivityId") long ActivityId);

    /**
     * 根据activityid获取商品品牌和品类
     *
     * @param activityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductInfoByActivityIdForBrandAndCategory(@Param("activityId") long activityId);

    /**
     * 根据productid获取规格信息
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductCatalogs(@Param("productId") String productId);

    /**
     * 获取商品规格列表
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductStockInfo(@Param("productId") String productId);

    /**
     * 获取商品规格列表
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductUser(@Param("productId") String productId);

    /**
     * 获取直播商品信息
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductLiveInfo(@Param("activityId") long activityId, @Param("productId") String productId);

    /**
     * 获取待同步的活动商品信息
     *
     * @param productInactivityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getActivityProducts(@Param("productInactivityId") long productInactivityId);


    /**
     * 获取待同步的活动商品规格列表信息
     *
     * @param productInactivityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getActivityProductCatalogs(@Param("productInactivityId") long productInactivityId);

    /**
     * 根据直播id获取直播商品排序信息
     *
     * @param activityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductsLiveSort(@Param("activityId") long activityId);

    /**
     * 更新商品业务凭据
     *
     * @param transactionInfo
     * @return
     */
    @TargetDataSource("productDataSource")
    int updateTransactionInfo(TransactionInfo transactionInfo);

    /**
     * 根据transactionId获取业务凭据信息
     *
     * @param transactionId
     * @return
     */
    @TargetDataSource("productDataSource")
    TransactionInfo getTransactionInfo(@Param("transactionId") long transactionId);

    /**
     * 获取需要补单的信息
     * @param readCount
     * @param timeLimit
     * @param retryLimit
     * @return
     */
    @TargetDataSource("productDataSource")
    List<TransactionInfo> getCompensationInfo(@Param("readCount") int readCount,@Param("timeLimit") int timeLimit,@Param("retryLimit") int retryLimit);

    /**
     * 获取商品橱窗推荐状态
     *
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductOnOffTop(@Param("productId") String productId);
}
