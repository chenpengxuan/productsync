package com.ymatou.productsync.domain.sqlrepo;

import com.ymatou.productsync.infrastructure.config.datasource.TargetDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * 单元测试sql数据查询相关
 * Created by zhangyong on 2017/2/7.
 */
public interface TestCommandQuery {
    /**
     * 获取橱窗推荐商品信息
     *
     * @param
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getTopProduct();

    /**
     * 获取不是橱窗推荐商品信息
     *
     * @param
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getNotTopProduct();

    /**
     * 取个现货商品
     *
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProduct();

    /**
     * 取个直播商品
     *
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getLiveProduct();

    /**
     * 取个活动商品
     *
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getActivityProduct();

    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getInvalidActivityProduct();
}
