package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.facade.model.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 修改商品待上架信息
 * Created by chenpengxuan on 2017/2/14.
 */
@Component("modifyReadyPutawayProductInfoExecutorConfig")
public class ModifyReadyPutawayProductInfoExecutorConfig implements ExecutorConfig {
    @Autowired
    private ModifyPutawayProductInfoExecutorConfig modifyPutawayProductInfoExecutorConfig;
    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ModifyReadyPutawayProductInfo;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        return modifyPutawayProductInfoExecutorConfig.loadSourceData(activityId,productId);
    }
}
