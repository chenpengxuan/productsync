package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataCreator;
import com.ymatou.productsync.domain.model.MongoData;
import org.springframework.stereotype.Component;

import java.util.*;

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
        List<Map<String, Object>> sourceData = new ArrayList<Map<String, Object>>();

        Map<String, Object> map = new HashMap();
        map.put("status", 0);
        map.put("istop", false);
        sourceData.add(map);

        Map<String, Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("spid", productId);
        matchConditionInfo.put("lid", activityId);
        mongoDataList.add(MongoDataCreator.CreateLiveProductUpdate(matchConditionInfo, sourceData));

        return mongoDataList;
    }

}
