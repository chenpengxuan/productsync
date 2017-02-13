package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 暂停销售
 * Created by zhujinfeng on 2017/2/8.
 */
@Component("suspendSaleExecutorConfig")
public class SuspendSaleExecutorConfig implements ExecutorConfig {

    @Override
    public CmdTypeEnum getCommand(){ return CmdTypeEnum.SuspendSale; }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> sourceData = new ArrayList<>();
        Map<String, Object> map = new HashMap();
        map.put("status", 0);
        map.put("istop", false);
        sourceData.add(map);
        mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(productId,activityId), sourceData));
        return mongoDataList;
    }

}
