package com.ymatou.productsync.domain.executor;

import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoOperationTypeEnum;
import com.ymatou.productsync.infrastructure.constants.Constants;

import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/7.
 * mongo数据指令包装器
 */
public  class MongoDataCreator {

    /**
     * 指令创建
     * @param tableName
     * @param operationType
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData Create(String tableName,
                                   MongoOperationTypeEnum operationType,
                                   Map<String,Object> matchCondition,
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
     * @param tableName
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData CreateAdd(String tableName,
                                         Map<String,Object> matchCondition,
                                         List<Map<String, Object>> updateData){
        return Create(tableName,MongoOperationTypeEnum.CREATE,matchCondition,updateData);
    }

    /**
     * 创建更新型
     * @param tableName
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData CreateUpdate(String tableName,
                                         Map<String,Object> matchCondition,
                                         List<Map<String, Object>> updateData){
        return Create(tableName,MongoOperationTypeEnum.UPDATE,matchCondition,updateData);
    }

    /**
     * 创建直播更新型
     * @param matchCondition
     * @param updateData
     * @return
     */

    public static MongoData CreateLiveUpdate(Map<String,Object> matchCondition,
                                         List<Map<String, Object>> updateData){
        return Create(Constants.LiveDb,MongoOperationTypeEnum.UPDATE,matchCondition,updateData);
    }

    /**
     * 创建商品更新型
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData CreateProductUpdate(Map<String,Object> matchCondition,
                                             List<Map<String, Object>> updateData){
        return Create(Constants.ProductDb,MongoOperationTypeEnum.UPDATE,matchCondition,updateData);
    }

    /**
     * 创建直播添加型
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData CreateLiveAdd(Map<String,Object> matchCondition,
                                             List<Map<String, Object>> updateData){
        return Create(Constants.LiveDb,MongoOperationTypeEnum.CREATE,matchCondition,updateData);
    }

    /**
     * 创建商品添加型
     * @param matchCondition
     * @param updateData
     * @return
     */
    public static MongoData CreateProductAdd(Map<String,Object> matchCondition,
                                          List<Map<String, Object>> updateData){
        return Create(Constants.ProductDb,MongoOperationTypeEnum.CREATE,matchCondition,updateData);
    }


    /**
     * 创建直播商品更新型
     * @param matchCondition
     * @param updateData
     * @return
     */

    public static MongoData CreateLiveProductUpdate(Map<String,Object> matchCondition,
                                             List<Map<String, Object>> updateData){
        return Create(Constants.LiveProudctDb,MongoOperationTypeEnum.UPDATE,matchCondition,updateData);
    }
}
