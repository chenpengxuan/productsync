package com.ymatou.productsync.domain.sqlrepo;

import com.ymatou.productsync.infrastructure.config.datasource.TargetDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * sql数据查询相关
 * Created by zhangyong on 2017/2/7.
 */
public interface LiveCommandQuery {
    /**
     * 获取直播相关信息
     *
     * @param ActivityId
     * @return
     */
    @TargetDataSource("liveDataSource")
    List<Map<String, Object>> getActivityInfo(@Param("ActivityId") long ActivityId);

    /**
     * 根据countryid取国家信息
     *
     * @param CountryId
     * @return
     */
    @TargetDataSource("ymtDataSource")
    List<Map<String, Object>> getCountryInfo(@Param("CountryId") int CountryId);

    /**
     * 根据activytid获取商品列表
     *
     * @param ActivityId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String, Object>> getProductInfoByActivityId(@Param("ActivityId") long ActivityId);
}
