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
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException{
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

        //前置条件检查
        if (sqlProductDataList == null || sqlProductDataList.isEmpty()){
            throw new BizException(ErrorCode.BIZFAIL,"getProductDetailInfo为空");
        }
        List<MongoData> mongoDataList = new ArrayList<>();
        //创建商品信息
            MapUtil.mapFieldToStringArray(sqlProductDataList, "pics", ",");
            //针对添加商品进直播的情况不能覆盖版本号
            if(activityId == 0){
                sqlProductDataList.parallelStream().findFirst().orElse(Collections.emptyMap()).put("ver", "1.001");
                sqlProductDataList.parallelStream().findFirst().orElse(Collections.emptyMap()).put("verupdate", new DateTime().toString(Utils.DEFAULT_DATE_FORMAT));
            }
            mongoDataList.add(MongoDataBuilder.createProductUpsert(MongoQueryBuilder.queryProductId(productId),sqlProductDataList));

        //创建规格信息
        if (sqlCatalogDataList != null && !sqlCatalogDataList.isEmpty()) {
            mongoDataList.add(MongoDataBuilder.createCatalogAdd(MapUtil.mapFieldArrayToNestedObj(sqlCatalogDataList,new String[]{"name","pic","value"},"props","cid")));
        }else{
            throw new BizException(ErrorCode.BIZFAIL,"getProductCatalogInfo为空");
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
        }else{
            throw new BizException(ErrorCode.BIZFAIL,"getProductDescInfo为空");
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
            mongoDataList.add(MongoDataBuilder.createProductLiveAdd(sqlProductInLiveDataList));
        }
        //更新直播信息
        if (sqlLiveDataList != null && !sqlLiveDataList.isEmpty()) {
            Object[] brands= sqlLiveDataList.parallelStream().map(t->t.get("sBrand")).toArray();
            sqlLiveDataList.clear();
            Map<String,Object> tempMap = new HashMap<>();
            tempMap.put("brands",brands);
            sqlLiveDataList.add(tempMap);
            mongoDataList.add(MongoDataBuilder.createLiveAdd(sqlLiveDataList));
        }
        return mongoDataList;
    }
}
