package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 添加直播
 * Created by zhangyong on 2017/2/7.
 */
@Component("createActivityExecutorConfig")
public class CreateActivityExecutorConfig implements ExecutorConfig {
    @Autowired
    private AddActivityExecutorConfig addActivityExecutorConfig;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.CreateActivity;
    }

    public List<MongoData> loadSourceData(long activityId, String productId) {
        return addActivityExecutorConfig.loadSourceData(activityId, productId);
    }
}
