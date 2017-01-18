package com.ymatou.product.service;

/**
 * 商品业务相关
 * Created by chenpengxuan on 2017/1/17.
 */
public interface IProductService {
    /**
     * 删除商品
     * @param liveId 直播id
     * @param productId 商品id
     * @param transactionId 业务凭据id
     * @return
     */
    boolean DeleteProduct(int liveId,String productId,int transactionId) throws Exception;
}
