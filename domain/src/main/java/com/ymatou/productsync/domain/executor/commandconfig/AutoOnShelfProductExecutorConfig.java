package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.joda.time.DateTime;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyong on 2017/2/9.
 */
@Component("autoOnShelfProductExecutorConfig")
public class AutoOnShelfProductExecutorConfig implements ExecutorConfig {
    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AutoOnShelf;
    }

    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        Map<String, Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("spid", productId);
        List<Map<String, Object>> updateData = new ArrayList<>();
        Map<String, Object> update = new HashMap();
        DateTime date = new DateTime();
        update.put("start", date.toString(Utils.DEFAULT_DATE_FORMAT));
        update.put("end", date.plusDays(7).toString(Utils.DEFAULT_DATE_FORMAT));
        updateData.add(update);
        mongoDataList.add(MongoDataBuilder.createProductUpdate(matchConditionInfo, updateData));
        return mongoDataList;
    }
}

