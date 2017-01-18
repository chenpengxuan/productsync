package com.ymatou.product.repository;

import com.ymatou.product.infrastructure.TargetDataSource;
import com.ymatou.product.vo.DeleteProductInfo;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * 商品数据仓储相关
 * Created by chenpengxuan on 2017/1/17.
 */
public interface IProductRepository {
    /**
     * 获取要删除的商品信息
     * @param productId
     * @return
     */
    @TargetDataSource("productDataSource")
    @Select("SELECT validStart AS [ValidStartTime]" +
            ",validEnd AS [VaildEndTime]" +
            ",[dListingTime] AS [NewStartTime]" +
            ",[dNewEndtime] AS [NewEndtime]" +
            ",[isNew] AS [IsNew]" +
            ",iAction as Action" +
            " FROM [dbo].Ymt_Products WITH(NOLOCK)" +
            " WHERE [sProductId] = #{productId}")
    DeleteProductInfo GetDeleteProductInfoByProductId(@Param("productId") String productId);
}
