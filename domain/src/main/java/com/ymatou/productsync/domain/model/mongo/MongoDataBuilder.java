package com.ymatou.productsync.domain.model.mongo;

import com.google.common.collect.Lists;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Created by chenfei on 2017/2/7.
 * mongo数据指令包装器
 */
@Component
public class MongoDataBuilder {

    @Autowired
    private MongoRepository mongoRepository;

    private static final String[] recordChangeTableArray = {Constants.ProductDb,
            Constants.CatalogDb,
            Constants.ActivityProductDb,
            Constants.LiveProudctDb};

    private static MongoRepository repository;

    @PostConstruct
    private void init(){
        repository = mongoRepository;
    }

    /**
     * 指令创建
     *
     * @param tableName
     * @param operationType
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData buildMongoData(String tableName,
                                           MongoOperationTypeEnum operationType,
                                           Map<String, Object> matchCondition,
                                           List<Map<String, Object>> updateData) {
        MongoData md = new MongoData();
        md.setTableName(tableName);
        md.setOperationType(operationType);
        md.setMatchCondition(matchCondition);
        md.setUpdateData(updateData);
        return md;
    }

    /**
     * 查询指令创建
     *
     * @param tableName
     * @param operationType
     * @param matchCondition
     * @param distinctKey
     * @return
     */
    public static MongoQueryData buildQueryMongoData(String tableName,
                                                     MongoOperationTypeEnum operationType,
                                                     Map<String, Object> matchCondition,
                                                     String distinctKey
    ) {
        MongoQueryData md = new MongoQueryData();
        md.setTableName(tableName);
        md.setOperationType(operationType);
        md.setMatchCondition(matchCondition);
        md.setDistinctKey(distinctKey);
        return md;
    }

    /**
     * 创建添加型
     *
     * @param tableName
     * @param updateData
     * @return
     */
    public static MongoData createAdd(String tableName,
                                      List<Map<String, Object>> updateData) {
        return buildMongoData(tableName, MongoOperationTypeEnum.CREATE, null, updateData);
    }

    /**
     * 创建更新型
     *
     * @param tableName
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData createUpdate(String tableName,
                                         Map<String, Object> matchCondition,
                                         List<Map<String, Object>> updateData) {
        return buildMongoData(tableName, MongoOperationTypeEnum.UPDATE, matchCondition, updateData);
    }

    /**
     * 创建更新型
     *
     * @param tableName
     * @param matchCondition
     * @return
     */
    public static MongoData createDelete(String tableName,
                                         Map<String, Object> matchCondition) {
        return buildMongoData(tableName, MongoOperationTypeEnum.DELETE, matchCondition, Lists.newArrayList());
    }

    /**
     * 创建直播更新型
     *
     * @param matchCondition
     * @param updateData
     * @return
     */

    public static MongoData createLiveUpdate(Map<String, Object> matchCondition,
                                             List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.LiveDb, MongoOperationTypeEnum.UPDATE, matchCondition, updateData);
    }

    /**
     * 创建直播更新型
     *
     * @param matchCondition
     * @param updateData
     * @return
     */

    public static MongoData createLiveUpsert(Map<String, Object> matchCondition,
                                             List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.LiveDb, MongoOperationTypeEnum.UPSERT, matchCondition, updateData);
    }

    /**
     * 创建商品更新型
     *
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData createProductUpdate(Map<String, Object> matchCondition,
                                                List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ProductDb, MongoOperationTypeEnum.UPDATE, matchCondition, updateData);
    }

    /**
     * 创建商品更新型
     *
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData createProductUpsert(Map<String, Object> matchCondition,
                                                List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ProductDb, MongoOperationTypeEnum.UPSERT, matchCondition, updateData);
    }

    /**
     * 查询单个商品信息
     *
     * @param matchCondition
     * @return
     */
    public static MongoQueryData querySingleProductInfo(Map<String, Object> matchCondition) {
        return buildQueryMongoData(Constants.ProductDb, MongoOperationTypeEnum.SELECTSINGLE, matchCondition, "");
    }

    /**
     * 创建直播添加型
     *
     * @param updateData
     * @return
     */
    public static MongoData createLiveAdd(
            List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.LiveDb, MongoOperationTypeEnum.CREATE, null, updateData);
    }

    /**
     * 创建商品添加型
     *
     * @param updateData
     * @return
     */
    public static MongoData createProductAdd(
            List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ProductDb, MongoOperationTypeEnum.CREATE, null, updateData);
    }

    /**
     * 创建直播商品更新型
     *
     * @param matchCondition
     * @param updateData
     * @return
     */

    public static MongoData createLiveProductUpdate(Map<String, Object> matchCondition,
                                                    List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.LiveProudctDb, MongoOperationTypeEnum.UPDATE, matchCondition, updateData);
    }

    /**
     * 创建商品规格添加型
     *
     * @param updateData
     * @return
     */
    public static MongoData createCatalogAdd(
            List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.CatalogDb, MongoOperationTypeEnum.CREATE, null, updateData);
    }

    /**
     * 创建商品规格更新型
     *
     * @param updateData
     * @return
     */
    public static MongoData createCatalogUpsert(Map<String, Object> matchCondition,
                                                List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.CatalogDb, MongoOperationTypeEnum.UPSERT, matchCondition, updateData);
    }

    /**
     * 更新商品规格库存
     *
     * @param updateData
     * @return
     */
    public static MongoData createCatalogUpdate(
            List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.CatalogDb, MongoOperationTypeEnum.UPDATE, null, updateData);
    }

    /**
     * 创建商品描述图文信息
     *
     * @param updateData
     * @return
     */
    public static MongoData createProductDescAdd(List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ProductDescriptionDb, MongoOperationTypeEnum.CREATE, null, updateData);
    }

    /**
     * 更新商品描述图文信息
     *
     * @param updateData
     * @return
     */
    public static MongoData createProductDescUpsert(Map<String, Object> matchCondition, List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ProductDescriptionDb, MongoOperationTypeEnum.UPSERT, matchCondition, updateData);
    }

    /**
     * 更新商品图文描述（2）
     *
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData createDescriptionsUpsert(Map<String, Object> matchCondition, List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ProductDescExtraDb, MongoOperationTypeEnum.UPSERT, matchCondition, updateData);
    }

    /**
     * 更新商品图文描述（2）
     *
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData createDescriptionsUpdate(Map<String, Object> matchCondition, List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ProductDescExtraDb, MongoOperationTypeEnum.UPDATE, matchCondition, updateData);
    }

    /**
     * 创建商品直播信息
     *
     * @param updateData
     * @return
     */
    public static MongoData createProductLiveAdd(List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.LiveProudctDb, MongoOperationTypeEnum.CREATE, null, updateData);
    }

    /**
     * 更新商品直播信息
     *
     * @param updateData
     * @return
     */
    public static MongoData createProductLiveUpsert(Map<String, Object> matchCondition, List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.LiveProudctDb, MongoOperationTypeEnum.UPSERT, matchCondition, updateData);
    }


    /**
     * 创建直播商品删除
     *
     * @param matchCondition
     * @return
     */

    public static MongoData createLiveProductDelete(Map<String, Object> matchCondition) {
        return buildMongoData(Constants.LiveProudctDb, MongoOperationTypeEnum.DELETE, matchCondition, null);
    }

    /**
     * 创建规格删除
     *
     * @param matchCondition
     * @param updateData
     * @return
     */

    public static MongoData createCatalogDelete(Map<String, Object> matchCondition,
                                                List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.CatalogDb, MongoOperationTypeEnum.DELETE, matchCondition, updateData);
    }

    /**
     * 同步活动商品数据
     *
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData syncActivityProducts(Map<String, Object> matchCondition,
                                                 List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ActivityProductDb, MongoOperationTypeEnum.UPSERT, matchCondition, updateData);
    }

    /**
     * 同步商品相关表时间戳
     *
     * @param mongoDataList key为productId的集合 value为key对应的表修改边界
     * @return
     */
    public static boolean syncProductRelatedTimeStamp(SyncByCommandReq req,List<MongoData> mongoDataList) {

       List<MongoData> timeStampList = mongoDataList
                .stream()
                .filter(z -> Arrays.asList(recordChangeTableArray).contains(z.getTableName()))
                .map(x -> {
                    MongoData tempData = new MongoData();
                    List<String> productIdList = new ArrayList<>();
                    //针对update delete操作
                    if(x.getMatchCondition() != null
                            && x.getMatchCondition().get("spid") != null){
                        if(x.getMatchCondition().get("spid") instanceof Map){
                            Map<String,Object> tempMap = (Map<String,Object>)x.getMatchCondition().get("spid");
                            productIdList.addAll((List<String>)tempMap.get("$in"));
                        }else{
                            productIdList.add(x.getMatchCondition().get("spid").toString());
                        }
                    }
                    else if(x.getUpdateData() != null){
                        List<String> tempProductIdList = x.getUpdateData()
                                .stream()
                                .filter(z -> z.get("spid") != null)
                                .map(xx -> xx.get("spid").toString())
                                .collect(Collectors.toList());
                        productIdList.addAll(tempProductIdList);
                    }
                    //针对涉及到相关表修改，但是不拿商品id作为查询匹配条件的情况
                    //例如规格表的修改
                    else{
                        productIdList.add(req.getProductId());
                    }
                    Map<String, Object> matchConditionMap = new HashMap<>();
                    Map<String, Object> tempProductIdMap = new HashMap<>();
                    tempProductIdMap.put("$in", productIdList);
                    matchConditionMap.put("spid", tempProductIdMap);
                    tempData.setMatchCondition(matchConditionMap);

                    Map<String, Object> updateData = new HashMap<>();
                    switch (x.getTableName()) {
                        case Constants.ProductDb:
                            updateData.put("sut", new Date());
                            break;
                        case Constants.CatalogDb:
                            updateData.put("cut", new Date());
                            break;
                        case Constants.LiveProudctDb:
                            updateData.put("lut", new Date());
                            break;
                        case Constants.ActivityProductDb:
                            updateData.put("aut", new Date());
                            break;
                        default:
                            break;
                    }
                    tempData.setUpdateData(Arrays.asList(updateData));

                    tempData.setTableName(Constants.ProductTimeStamp);

                    tempData.setOperationType(MongoOperationTypeEnum.UPSERT);

                    return tempData;
                })
                .collect(Collectors.toList());

        return repository.excuteMongo(timeStampList);
    }

}
