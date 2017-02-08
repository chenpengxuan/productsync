package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataCreator;
import com.ymatou.productsync.domain.executor.MongoQueryCreator;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by chenfei on 2017/2/7.
 * 关闭直播
 */
@Component("closeActivityExecutorConfig")
public class CloseActivityExecutorConfig implements ExecutorConfig {

    @Autowired
    private ModifyActivityExecutorConfig modifyActivityExecutorConfig;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.CloseActivity;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        return modifyActivityExecutorConfig.loadSourceData(activityId,productId);
    }
}
