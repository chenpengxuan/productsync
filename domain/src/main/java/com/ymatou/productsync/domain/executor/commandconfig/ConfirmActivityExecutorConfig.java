package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 添加直播
 * Created by zhangyong on 2017/2/7.
 */
@Component("confirmActivityExecutorConfig")
public class ConfirmActivityExecutorConfig implements ExecutorConfig {
    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ConfirmActivity;
    }

    @Autowired
    private AddActivityExecutorConfig addActivityExecutorConfig;

    public List<MongoData> loadSourceData(long activityId, String productId) {
        return addActivityExecutorConfig.loadSourceData(activityId, productId);
    }
}
