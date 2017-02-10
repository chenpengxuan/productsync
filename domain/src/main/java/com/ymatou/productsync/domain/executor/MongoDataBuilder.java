package com.ymatou.productsync.domain.executor;

import com.google.common.collect.Lists;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoOperationTypeEnum;
import com.ymatou.productsync.infrastructure.constants.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/7.
 * mongo数据指令包装器
 */
public class MongoDataBuilder {

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
    public static MongoData createProductDescUpsert(List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ProductDescriptionDb, MongoOperationTypeEnum.UPSERT, null, updateData);
    }

    /**
     * 创建商品直播信息
     * @param updateData
     * @return
     */
    public static MongoData createProductLiveAdd(List<Map<String, Object>> updateData){
        return buildMongoData(Constants.LiveProudctDb,MongoOperationTypeEnum.CREATE,null,updateData);
    }

    /**
     * 更新商品直播信息
     * @param updateData
     * @return
     */
    public static MongoData createProductLiveUpert(List<Map<String, Object>> updateData){
        return buildMongoData(Constants.LiveProudctDb,MongoOperationTypeEnum.UPSERT,null,updateData);
    }


    /**
     * 创建直播商品删除
     *
     * @param matchCondition
     * @param updateData
     * @return
     */

    public static MongoData createLiveProductDelete(Map<String, Object> matchCondition,
                                                    List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.LiveProudctDb, MongoOperationTypeEnum.DELETE, matchCondition, updateData);
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
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData syncActivityProducts(Map<String,Object> matchCondition,
                                                 List<Map<String, Object>> updateData) {
        return buildMongoData(Constants.ActivityProductDb, MongoOperationTypeEnum.UPSERT, matchCondition, updateData);
    }

}
