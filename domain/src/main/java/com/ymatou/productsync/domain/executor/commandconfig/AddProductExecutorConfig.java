package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
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

    @Autowired
    private MongoRepository mongoRepository;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AddProduct;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
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
        if (sqlProductDataList == null || sqlProductDataList.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, "getProductDetailInfo为空");
        }
        if (sqlCatalogDataList == null || sqlCatalogDataList.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, "getProductCatalogInfo为空");
        }
        if (sqlProductDescDataList == null || sqlProductDescDataList.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, "getProductDescInfo为空");
        }
        if (activityId > 0 && (sqlProductInLiveDataList == null || sqlProductInLiveDataList.isEmpty())) {
            throw new BizException(ErrorCode.BIZFAIL, "getProductLiveInfo为空");
        }
        if (activityId > 0 && (sqlLiveDataList == null || sqlLiveDataList.isEmpty())) {
            throw new BizException(ErrorCode.BIZFAIL, "getProductInfoByActivityId为空");
        }

        List<MongoData> mongoDataList = new ArrayList<>();

        //创建商品信息
        MapUtil.mapFieldToStringArray(sqlProductDataList, "pics", ",");
        Map<String, Object> tempProductDataMap = sqlProductDataList.stream().findFirst().orElse(Collections.emptyMap());
        tempProductDataMap.replace("newdesc", tempProductDataMap.get("newdesc"), ((int) tempProductDataMap.get("newdesc")) == 1);
        //针对添加商品进直播的情况不能覆盖版本号,如果商品已经存在的话，则不更新商品快照信息
        if (mongoRepository.queryMongo(MongoDataBuilder.querySingleProductInfo(MongoQueryBuilder.queryProductId(productId)))
                .parallelStream().findFirst().orElse(Collections.emptyMap()).isEmpty()) {
            sqlProductDataList.parallelStream().findFirst().orElse(Collections.emptyMap()).put("ver", "1.001");
            sqlProductDataList.parallelStream().findFirst().orElse(Collections.emptyMap()).put("verupdate", new DateTime().toString(Utils.DEFAULT_DATE_FORMAT));
        }
        mongoDataList.add(MongoDataBuilder.createProductUpsert(MongoQueryBuilder.queryProductId(productId), sqlProductDataList));

        //创建规格信息 先删除再更新
        mongoDataList.add(MongoDataBuilder.createCatalogDelete(MongoQueryBuilder.queryProductId(productId),null));
        mongoDataList.add(MongoDataBuilder.createCatalogAdd(MapUtil.mapFieldArrayToNestedObj(sqlCatalogDataList, new String[]{"name", "pic", "value"}, "props", "cid")));

        //创建商品图文描述信息
        Map<String, Object> tempDescMap = new HashMap<>();
        tempDescMap.putAll(sqlProductDescDataList.parallelStream().findFirst().orElse(Collections.emptyMap()));
        tempDescMap.remove("pic");
        tempDescMap.put("pics", sqlProductDescDataList.parallelStream().map(x -> x.get("pic")).toArray());
        sqlProductDescDataList.clear();
        sqlProductDescDataList.add(tempDescMap);
        mongoDataList.add(MongoDataBuilder.createProductDescAdd(sqlProductDescDataList));

        //针对添加是商品进直播与直播中添加商品的场景
        if (activityId > 0) {
            //创建直播商品信息
            Map<String, Object> tempLiveProductMap = sqlProductInLiveDataList.parallelStream().findFirst().orElse(Collections.emptyMap());
            Map<String, Object> productMap = sqlProductDataList.parallelStream().findFirst().orElse(Collections.emptyMap());
            tempLiveProductMap.put("bid", productMap.get("bid"));
            tempLiveProductMap.put("mcatid", productMap.get("mcatid"));
            tempLiveProductMap.put("mcatname", productMap.get("mcatname"));
            tempLiveProductMap.put("scatid", productMap.get("scatid"));
            tempLiveProductMap.put("scatname", productMap.get("scatname"));
            tempLiveProductMap.put("tcatid", productMap.get("tcatid"));
            tempLiveProductMap.put("tcatname", productMap.get("tcatname"));
            tempLiveProductMap.put("brand", productMap.get("brand"));
            tempLiveProductMap.put("ebrand", productMap.get("ebrand"));
            tempLiveProductMap.put("comments", 0);
            mongoDataList.add(MongoDataBuilder.createProductLiveAdd(sqlProductInLiveDataList));

            //更新直播信息
            Object[] brands = sqlLiveDataList.parallelStream().map(t -> t.get("sBrand")).toArray();
            sqlLiveDataList.clear();
            Map<String, Object> tempLiveMap = new HashMap<>();
            tempLiveMap.put("brands", brands);
            sqlLiveDataList.add(tempLiveMap);
            mongoDataList.add(MongoDataBuilder.createLiveUpsert(MongoQueryBuilder.queryLiveId(activityId), sqlLiveDataList));
        }
        return mongoDataList;
    }
}
