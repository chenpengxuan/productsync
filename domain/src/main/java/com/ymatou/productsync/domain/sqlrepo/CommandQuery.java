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
//    @Select("SELECT top 1 "+
//            "        isTop" +
//            "        FROM dbo.Ymt_Products" +
//            "        WHERE sProductId = #{productId}")
    List<List<Map<String,Object>>> setTopProduct(@Param("productId") String productId);
}
