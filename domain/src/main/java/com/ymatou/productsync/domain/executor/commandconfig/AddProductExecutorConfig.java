package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.joda.time.DateTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 添加商品
 * Created by chenpengxuan on 2017/2/8.
 */
@Component("addProductExecutorConfig")
public class AddProductExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Autowired
    private LiveCommandQuery liveCommandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AddProduct;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        //商品信息
        List<Map<String, Object>> sqlProductDataList = commandQuery.getProductDetailInfo(productId);
        //商品规格信息
        List<Map<String, Object>> sqlCatalogDataList = commandQuery.getProductCatalogInfo(productId);
        //商品图文描述
        List<Map<String, Object>> sqlProductDescDataList = commandQuery.getProductDescInfo(productId);
        //商品直播信息
        List<Map<String, Object>> sqlProductInLiveDataList = commandQuery.getProductLiveInfo(activityId, productId);
        //直播信息
        List<Map<String, Object>> sqlLiveDataList = liveCommandQuery.getProductInfoByActivityId(activityId);
        List<MongoData> mongoDataList = new ArrayList<>();
        //创建商品信息
        if (sqlProductDataList != null && !sqlProductDataList.isEmpty()) {
            MapUtil.MapFieldToStringArray(sqlProductDataList, "pics", ",");
            sqlProductDataList.parallelStream().findFirst().orElse(Collections.emptyMap()).put("ver", "1.001");
            sqlProductDataList.parallelStream().findFirst().orElse(Collections.emptyMap()).put("verupdate", new DateTime().toString(Utils.DEFAULT_DATE_FORMAT));
            mongoDataList.add(MongoDataBuilder.createProductAdd(sqlProductDataList));
        }
        //创建规格信息
        if (sqlCatalogDataList != null && !sqlCatalogDataList.isEmpty()) {
            List<Map<String, Object>> tempSqlCatalogDataList = new ArrayList<>();
            List<Map<String, Object>> sqlCatalogPropertyDataList = new ArrayList<>();
            sqlCatalogDataList.stream().forEach(data -> {
                if (!tempSqlCatalogDataList.stream().anyMatch(x -> x.containsValue(data.get("cid")))) {
                    sqlCatalogPropertyDataList.clear();
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.putAll(data);
                    tempMap.remove("name");
                    tempMap.remove("pic");
                    tempMap.remove("value");
                    Map<String, Object> tempPropertyMap = new HashMap<>();
                    tempPropertyMap.put("name", data.get("name"));
                    tempPropertyMap.put("pic", data.get("pic"));
                    tempPropertyMap.put("value", data.get("value"));
                    sqlCatalogPropertyDataList.add(tempPropertyMap);
                    tempMap.put("props", sqlCatalogPropertyDataList);
                    tempSqlCatalogDataList.add(tempMap);
                } else {
                    List<Map<String, Object>> tempSqlCatalogPropertyDataList = (List<Map<String, Object>>) tempSqlCatalogDataList.parallelStream()
                            .filter(x -> x.containsValue(data.get("cid"))).findFirst()
                            .orElse(Collections.emptyMap()).get("props");
                    Map<String, Object> tempPropertyMap = new HashMap<>();
                    tempPropertyMap.put("name", data.get("name"));
                    tempPropertyMap.put("pic", data.get("pic"));
                    tempPropertyMap.put("value", data.get("value"));
                    tempSqlCatalogPropertyDataList.add(tempPropertyMap);
                }
            });
            mongoDataList.add(MongoDataBuilder.createCatalogAdd(tempSqlCatalogDataList));
        }
        //创建商品图文描述信息
        if (sqlProductDescDataList != null && !sqlProductDescDataList.isEmpty()) {
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.putAll(sqlProductDescDataList.parallelStream().findFirst().orElse(Collections.emptyMap()));
            tempMap.remove("pic");
            tempMap.put("pics", sqlProductDescDataList.parallelStream().map(x -> x.get("pic")).toArray());
            sqlProductDescDataList.clear();
            sqlProductDescDataList.add(tempMap);
            mongoDataList.add(MongoDataBuilder.createProductDescAdd(sqlProductDescDataList));
        }
        //创建直播商品信息
        if(sqlProductInLiveDataList != null && !sqlProductInLiveDataList.isEmpty()){
            Map<String,Object> tempMap = sqlProductInLiveDataList.parallelStream().findFirst().orElse(Collections.emptyMap());
            Map<String,Object> productMap = sqlProductDataList.parallelStream().findFirst().orElse(Collections.emptyMap());
            tempMap.put("bid",productMap.get("bid"));
            tempMap.put("mcatid",productMap.get("mcatid"));
            tempMap.put("mcatname",productMap.get("mcatname"));
            tempMap.put("scatid",productMap.get("scatid"));
            tempMap.put("scatname",productMap.get("scatname"));
            tempMap.put("tcatid",productMap.get("tcatid"));
            tempMap.put("tcatname",productMap.get("tcatname"));
            tempMap.put("brand",productMap.get("brand"));
            tempMap.put("ebrand",productMap.get("ebrand"));
            tempMap.put("comments",0);
            tempMap.put("lname","");
            mongoDataList.add(MongoDataBuilder.createProductLiveAdd(sqlProductDescDataList));
        }
        //更新直播信息
        if (sqlLiveDataList != null && !sqlLiveDataList.isEmpty()) {
            Object[] brands= sqlLiveDataList.parallelStream().map(t->t.get("sBrand")).toArray();
            sqlLiveDataList.clear();
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.put("brands",brands);
            sqlLiveDataList.add(tempMap);
        }
        mongoDataList.add(MongoDataBuilder.createLiveUpsert(MongoQueryBuilder.queryLiveId(activityId),sqlLiveDataList));
        return mongoDataList;
    }
}
