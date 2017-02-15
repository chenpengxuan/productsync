package com.ymatou.productsync.facade;

import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.facade.model.resp.BaseResponse;

/**
 * Created by chenpengxuan on 2017/1/19.
 */
public interface SyncCommandFacade {
    /**
     * 根据场景命令同步
     * <p>
     * 根据场景获得需要更新的字段以及需要同步的索引
     *
     * @param req 基于业务场景的请求
     */
    String syncByCommand(SyncByCommandReq req);

    /**
     * Get方式同步调用
     * @param req
     * @return
     */
    BaseResponse updateByCommandSync(SyncByCommandReq req);

    /**
     * 更新商品快照信息
     * @param productId
     * @param snapshotVersion
     * @return
     */
    BaseResponse updateProductSnapShot(String productId,String snapshotVersion);
}
