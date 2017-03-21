package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.facade.model.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 修改上架商品信息
 * Created by chenpengxuan on 2017/2/8.
 */
@Component("modifyPutawayProductInfoExecutorConfig")
public class ModifyPutawayProductInfoExecutorConfig implements ExecutorConfig {
    @Autowired
    private AddProductExecutorConfig addProductExecutorConfig;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ModifyPutawayProductInfo;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException{
       return addProductExecutorConfig.loadSourceData(activityId,productId);
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {
        return addProductExecutorConfig.getProductChangeRangeInfo();
    }
}
