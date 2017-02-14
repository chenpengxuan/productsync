package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.*;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

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
            Map<String, Object> activity = sqlDataList.parallelStream().findFirst().orElse(Collections.emptyMap());
            int countryId = Integer.parseInt(activity.get("iCountryId").toString());
            activity.remove("iCountryId");
            List<Map<String, Object>> country = commandQuery.getCountryInfo(countryId);
            if (country != null && !country.isEmpty()) {
                Map<String, Object> con = country.parallelStream().findFirst().orElse(Collections.emptyMap());
                activity.put("country", con.get("sCountryNameZh"));
                activity.put("flag", con.get("sFlag"));
            }
            List<Map<String, Object>> products = commandQuery.getProductInfoByActivityId(activityId);
            if (products != null && !products.isEmpty()) {
                products.stream().forEach(t -> t.remove("dAddTime"));
                Object[] brands = products.parallelStream().distinct().map(t -> t.get("sBrand")).toArray();
                activity.put("brands", brands);
            }
            Map<String, Object> matchConditionInfo = new HashMap();
            matchConditionInfo.put("lid", activityId);
            mongoDataList.add(MongoDataBuilder.createLiveUpsert(MongoQueryBuilder.queryLiveId(activityId),sqlDataList));
        }else
        {
            throw new BizException(ErrorCode.BIZFAIL,"getActivityInfo为空");
        }
        return mongoDataList;
    }
}
