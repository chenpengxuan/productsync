package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoOperationTypeEnum;
import com.ymatou.productsync.infrastructure.constants.Constants;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/7.
 */
public class CloseActivityExecutorConfig implements ExecutorConfig {
    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AddActivity;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        MongoData mongoData = new MongoData();
        mongoData.setOperationType(MongoOperationTypeEnum.UPDATE);
        //设置mongo表名
        mongoData.setTableName(Constants.LiveDb);
        //设置匹配条件
        Map<String,Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("spid",productId);
        mongoData.setMatchCondition(matchConditionInfo);
        //设置要更新的数据

        mongoDataList.add(mongoData);
        return mongoDataList;
    }
}
