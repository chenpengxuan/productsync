package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.facade.model.BizException;

import java.util.List;

/**
 * Created by chenfei on 2017/2/14.
 */
public class DeleteActivityExecutorConfig implements ExecutorConfig {
    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.DeleteActivity;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        return null;
    }
}
