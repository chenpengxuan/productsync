package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by zhangyong on 2017/2/10.
 */
@Component("setOnShelfUpdateStockNumExecutorConfig")
public class SetOnShelfUpdateStockNumExecutorConfig implements ExecutorConfig {
    @Autowired
    private AddProductExecutorConfig addProductExecutorConfig;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.SetOnShelfUpdateStockNum;
    }

    public List<MongoData> loadSourceData(long activityId, String productId) {
        return addProductExecutorConfig.loadSourceData(activityId, productId);
    }
}
