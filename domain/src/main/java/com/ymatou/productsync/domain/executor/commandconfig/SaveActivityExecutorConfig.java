package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * Created by chenfei on 2017/2/8.
 * 保存直播
 */
@Component("saveActivityExecutorConfig")
public class SaveActivityExecutorConfig implements ExecutorConfig {

    @Autowired
    private ModifyActivityExecutorConfig modifyActivityExecutorConfig;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.SaveActivity;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        return modifyActivityExecutorConfig.loadSourceData(activityId,productId);
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {
        return modifyActivityExecutorConfig.getProductChangeRangeInfo();
    }
}