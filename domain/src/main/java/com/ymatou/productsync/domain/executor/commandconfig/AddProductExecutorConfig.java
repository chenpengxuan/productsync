package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
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
public class AddProductExecutorConfig implements ExecutorConfig{
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AddProduct;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        //商品信息
        List<Map<String,Object>> sqlProductDataList = commandQuery.getProductDetailInfo(productId);
        //商品规格信息
        List<Map<String,Object>> sqlCatalogDataList = commandQuery.getProductCatalogInfo(productId);
        //商品
        List<Map<String,Object>> sqlProductDescDataList = commandQuery.getProductDescInfo(productId);
        List<MongoData> mongoDataList = new ArrayList<>();
        //创建商品信息
        if(sqlProductDataList != null && !sqlProductDataList.isEmpty()){
            MapUtil.MapFieldToStringArray(sqlProductDataList,"pics",",");
            sqlProductDataList.get(0).put("ver","1.001");
            sqlProductDataList.get(0).put("verupdate",new DateTime().toString(Utils.DEFAULT_DATE_FORMAT));
            mongoDataList.add(MongoDataBuilder.createProductAdd(sqlProductDataList));
        }
        //创建规格信息
        if(sqlCatalogDataList != null && !sqlCatalogDataList.isEmpty()){
            List<Map<String,Object>> tempSqlCatalogDataList = new ArrayList<>();
            List<Map<String,Object>> sqlCatalogPropertyDataList = new ArrayList<>();
            sqlCatalogDataList.parallelStream().forEach(data ->{
                if(!tempSqlCatalogDataList.parallelStream().anyMatch(x -> x.containsValue(data.get("cid")))) {
                    sqlCatalogPropertyDataList.clear();
                    Map<String, Object> tempMap = new HashMap<>();
                    tempMap.putAll(data);
                    tempMap.remove("name");
                    tempMap.remove("pic");
                    tempMap.remove("value");
                    Map<String, Object> tempPropertyMap = new HashMap<>();
                    tempPropertyMap.put("name",data.get("name"));
                    tempPropertyMap.put("pic",data.get("pic"));
                    tempPropertyMap.put("value",data.get("value"));
                    sqlCatalogPropertyDataList.add(tempPropertyMap);
                    tempMap.put("props",sqlCatalogPropertyDataList);
                    tempSqlCatalogDataList.add(tempMap);
                }
               else{
                    List<Map<String,Object>> tempSqlCatalogPropertyDataList = (List<Map<String, Object>>) tempSqlCatalogDataList.parallelStream()
                            .filter(x -> x.containsValue(data.get("cid"))).findFirst()
                            .orElse(Collections.emptyMap()).get("props");
                    Map<String, Object> tempPropertyMap = new HashMap<>();
                    tempPropertyMap.put("name",data.get("name"));
                    tempPropertyMap.put("pic",data.get("pic"));
                    tempPropertyMap.put("value",data.get("value"));
                    tempSqlCatalogPropertyDataList.add(tempPropertyMap);
                }
            });
            mongoDataList.add(MongoDataBuilder.createCatalogAdd(tempSqlCatalogDataList));
        }
        //创建商品图文描述信息
        if(sqlProductDescDataList != null && !sqlProductDescDataList.isEmpty()){
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.putAll(sqlProductDescDataList.parallelStream().findFirst().orElse(Collections.emptyMap()));
            tempMap.remove("pic");
            tempMap.put("pics",sqlProductDescDataList.parallelStream().map(x -> x.get("pic")));
            sqlProductDescDataList.clear();
            sqlProductDescDataList.add(tempMap);
            mongoDataList.add(MongoDataBuilder.createProductDescAdd(sqlProductDescDataList));
        }
        return mongoDataList;
    }
}
