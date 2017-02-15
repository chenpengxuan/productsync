package com.ymatou.productsync.facade.model.req;

/**
 * 更新商品快照
 * Created by chenpengxuan on 2016/12/9.
 */
public class UpdateProductSnapShotReq extends BaseRequest {
    /**
     * 商品id
     */
    protected String productId;

    /**
     * 商品快照版本号
     */
    protected String snapShotVersion;
}
