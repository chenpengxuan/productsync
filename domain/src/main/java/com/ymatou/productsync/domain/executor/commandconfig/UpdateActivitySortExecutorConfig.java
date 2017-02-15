package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 直播中商品排序
 * Created by chenpengxuan on 2017/2/15.
 */
@Component("updateActivitySortExecutorConfig")
public class UpdateActivitySortExecutorConfig implements ExecutorConfig{
    @Autowired
    private LiveCommandQuery liveCommandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.UpdateActivitySort;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        return null;
    }
}
