package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import net.sourceforge.jtds.jdbc.DateTime;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;
import java.util.*;

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
        Date date = new Date();
        update.put("start", new Timestamp(date.getTime()));
        update.put("end", new Timestamp(date.getTime() + 7 * 24 * 60 * 60 * 1000));
        updateData.add(update);
        mongoDataList.add(MongoDataBuilder.createProductUpdate(matchConditionInfo, updateData));
        return mongoDataList;
    }
}

