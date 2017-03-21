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
 * 同步商品主图列表 - DeleteProductPics
 * Created by zhujinfeng on 2017/2/8.
 */
@Component("deleteProductPicsExecutorConfig")
public class DeleteProductPicsExecutorConfig implements ExecutorConfig {

    @Autowired
    private AddProductPicsExecutorConfig addProductPicsExecutorConfig;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.DeleteProductPics;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        return addProductPicsExecutorConfig.loadSourceData(activityId, productId);
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {
        return addProductPicsExecutorConfig.getProductChangeRangeInfo();
    }
}
