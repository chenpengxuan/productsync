package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 设置、取消橱窗商品场景同步器设定
 * Created by chenpengxuan on 2017/1/23.
 */
@Component("setOnTopExecutorConfig")
public class SetOnTopExecutorConfig implements ExecutorConfig{
    @Autowired
    private SetOffTopExecutorConfig setOffTopExecutorConfig;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.SetOnTop;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
            return setOffTopExecutorConfig.loadSourceData(activityId,productId);
    }
}
