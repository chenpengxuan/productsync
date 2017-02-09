package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoOperationTypeEnum;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
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

    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> sqlDataList = commandQuery.getActivityInfo(activityId);
        if (sqlDataList != null && !sqlDataList.isEmpty()) {
            Map<String, Object> activity = sqlDataList.get(0);
            int countryId = Integer.parseInt(activity.get("iCountryId").toString());
            activity.remove("iCountryId");
            List<Map<String, Object>> country = commandQuery.getCountryInfo(countryId);
            if (country != null && !country.isEmpty()) {
                Map<String, Object> con = country.get(0);
                if (con != null) {
                    activity.put("country", con.get("sCountryNameZh"));
                    activity.put("flag", con.get("sFlag"));
                }
            }
            List<Map<String, Object>> products = commandQuery.getProductInfoByActivityId(activityId);
            if (products != null && !products.isEmpty()) {
                products.stream().forEach(t -> t.remove("dAddTime"));
                Object[] brands= products.parallelStream().map(t->t.get("sBrand")).toArray();
                activity.put("brands", brands);
            }
            MongoData mongoData = new MongoData();
            mongoData.setTableName(Constants.LiveDb);
            Map<String, Object> matchConditionInfo = new HashMap();
            matchConditionInfo.put("lid", activityId);
            mongoData.setMatchCondition(matchConditionInfo);
            mongoData.setOperationType(MongoOperationTypeEnum.UPSERT);
            mongoData.setUpdateData(sqlDataList);
            mongoDataList.add(mongoData);
        }
        return mongoDataList;
    }


}
