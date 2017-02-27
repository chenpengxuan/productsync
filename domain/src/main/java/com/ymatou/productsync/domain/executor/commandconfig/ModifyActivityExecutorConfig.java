package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * Created by chenfei on 2017/2/8.
 * 修改直播
 */
@Component("modifyActivityExecutorConfig")
public class ModifyActivityExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;
    @Autowired
    private LiveCommandQuery liveCommandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ModifyActivity;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        ///1.直播数据更新
        List<Map<String, Object>> mapList = liveCommandQuery.getActivityInfo(activityId);
        if (mapList == null || mapList.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, this.getCommand() + "-getActivityInfo为空");
        }

        //处理brands,country,flag
        //MapUtil.MapFieldToStringArray(mapList, "brands", ",");
        Map<String, Object> activity = mapList.get(0);
        int countryId = Integer.parseInt(activity.get("iCountryId").toString());
        activity.remove("iCountryId");
        Optional<Map<String, Object>> country = liveCommandQuery.getCountryInfo(countryId).stream().findFirst();
        if (country.isPresent()) {
            Map<String, Object> con = country.get();
            activity.put("country", con.get("sCountryNameZh"));
            activity.put("flag", con.get("sFlag"));
        }
        List<Map<String, Object>> products = liveCommandQuery.getProductInfoByActivityId(activityId);
        if (products != null && !products.isEmpty()) {
            products.stream().forEach(t -> t.remove("dAddTime"));
            Object[] brands = products.stream().map(t -> t.get("sBrand")).distinct().toArray();
            activity.put("brands", brands);
        }

        //设置要更新的数据
        MongoData liveMongoData = MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(activityId), mapList);
        mongoDataList.add(liveMongoData);

        ///2.直播商品数据更新 --
        List<Map<String, Object>> liveProductMapList = commandQuery.getLiveProductByActivityId(activityId);
        List<Map<String, Object>> productMapList = commandQuery.getProductNewTimeByActivityId(activityId);
        if (liveProductMapList != null) {
            liveProductMapList.stream().forEach(liveProductItem -> {
                Object pid = liveProductItem.get("spid");
                Map<String, Object> liveProductCondition = MongoQueryBuilder.queryProductIdAndLiveId(pid.toString(),activityId);
                MongoData liveProductMongoData = MongoDataBuilder.createLiveProductUpdate(liveProductCondition, MapUtil.mapToList(liveProductItem));
                ///2.商品数据更新
                if (productMapList != null && productMapList.stream().anyMatch(p -> p.containsValue(pid))) {
                    Map<String, Object> pidCondition = MongoQueryBuilder.queryProductId(pid.toString());
                    List<Map<String, Object>> productData = productMapList.stream().filter(p -> p.containsValue(pid)).collect(Collectors.toList());
                    MongoData productMongoData = MongoDataBuilder.createProductUpdate(pidCondition, productData);
                    mongoDataList.add(productMongoData);
                }
                mongoDataList.add(liveProductMongoData);
            });
        }
        return mongoDataList;
    }
}
