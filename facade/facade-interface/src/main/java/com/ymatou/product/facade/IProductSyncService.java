package com.ymatou.product.facade;

import com.ymatou.product.basemodel.BaseResponse;
import com.ymatou.product.basemodel.ProductSyncRequest;
/**
 * 商品同步服务 facade interface
 * Created by chenpengxuan on 2017/1/16.
 */
public interface IProductSyncService {
    /**
     * 商品同步facade
     * @param request
     * @return
     */
    BaseResponse productSync(ProductSyncRequest request);
}
