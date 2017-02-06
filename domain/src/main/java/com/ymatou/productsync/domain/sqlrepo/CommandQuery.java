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
     * 商品设置、取消橱窗商品
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    List<Map<String,Object>> setTopProduct(@Param("productId") String productId);

    @TargetDataSource("apolloLogDataSource")
    List<List<Map<String,Object>>> setTopProduct2(@Param("productId") String productId);

    @TargetDataSource("liveDataSource")
    List<List<Map<String,Object>>> setTopProduct3(@Param("liveId") int liveId);
}
