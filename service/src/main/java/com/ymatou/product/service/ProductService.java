package com.ymatou.product.service;

import org.springframework.stereotype.Service;

/**
 * 商品业务相关
 * Created by chenpengxuan on 2017/1/17.
 */
@Service
public class ProductService implements  IProductService{

    @Override
    public boolean DeleteProduct(int liveId, String productId, int transactionId) throws Exception {
        return false;
    }
}
