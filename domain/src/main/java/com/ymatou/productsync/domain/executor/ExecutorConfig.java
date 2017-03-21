package com.ymatou.productsync.domain.executor;

import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.facade.model.BizException;

import java.util.List;

/**
 * Created by chenpengxuan on 2017/1/19.
 */
public interface ExecutorConfig {
    /**
     * 获取指令类型
     *
     * @return
     */
    CmdTypeEnum getCommand();

    /**
     * 获取待同步数据
     * @param activityId
     * @param productId
     * @return
     */
    List<MongoData> loadSourceData(long activityId, String productId) throws BizException;

    /**
     * 获取商品相关变更边界信息
     * @return
     */
    ProductChangedRange getProductChangeRangeInfo();
}
