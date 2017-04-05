package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 添加直播
 * Created by zhangyong on 2017/2/7.
 */
@Component("addActivityExecutorConfig")
public class AddActivityExecutorConfig implements ExecutorConfig {
    @Autowired
    private LiveCommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AddActivity;
    }

    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> sqlDataList = commandQuery.getActivityInfo(activityId);
        if (sqlDataList != null && !sqlDataList.isEmpty()) {
            Map<String, Object> activity = sqlDataList.stream().findFirst().orElse(Collections.emptyMap());
            int countryId = Integer.parseInt(activity.get("iCountryId") != null ? activity.get("iCountryId").toString():"0");
            activity.remove("iCountryId");
            List<Map<String, Object>> country = commandQuery.getCountryInfo(countryId);
            if (country != null && !country.isEmpty()) {
                Map<String, Object> con = country.stream().findFirst().orElse(Collections.emptyMap());
                activity.put("country", con.get("sCountryNameZh"));
                activity.put("flag", con.get("sFlag"));
            }
            List<Map<String, Object>> products = commandQuery.getProductInfoByActivityId(activityId);
            if (products != null && !products.isEmpty()) {
                Object[] brands = products.stream().map(t -> t.get("sBrand")).distinct().toArray();
                activity.put("brands", brands);
            }
            mongoDataList.add(MongoDataBuilder.createLiveUpsert(MongoQueryBuilder.queryLiveId(activityId), sqlDataList));
        } else {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), "getActivityInfo为空");
        }
        return mongoDataList;
    }
}
