package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 修改商品活动价
 * Created by zhangyong on 2017/2/10.
 */
@Component("modifyActivityPriceExecutorConfig")
public class ModifyActivityPriceExecutorConfig implements ExecutorConfig {

    @Autowired
    private SyncActivityProductExecutorConfig syncActivityProductExecutorConfig;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ModifyActivityPrice;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        return syncActivityProductExecutorConfig.loadSourceData(activityId, productId);
    }
}
