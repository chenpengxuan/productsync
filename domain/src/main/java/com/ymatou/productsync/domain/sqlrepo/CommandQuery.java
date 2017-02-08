package com.ymatou.productsync.domain.sqlrepo;

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
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String,Object>> getLiveProductTop(@Param("productId") String productId, @Param("activityId") long activityId);


    /**
     * 根据直播id获取直播基本信息
     * @param activityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String,Object>> getLiveProductByActivityId(@Param("activityId") long activityId);

    /**
     * 根据直播id获取商品新品信息
     * @param activityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String,Object>> getProductNewTimeByActivityId(@Param("activityId") long activityId);


    /**
     * 获取待删除的商品-cf
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String,Object>> getDeleteProducts(@Param("productId") String productId);


    /**
     * 获取直播商品时间-cf
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String,Object>> getLiveProductTime(@Param("productId") String productId,@Param("activityId") long activityId);

    /**
     * 根据商品id获取商品详情
     * @param activityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String,Object>> getProductDetailInfo(@Param("productId") String activityId);
}
