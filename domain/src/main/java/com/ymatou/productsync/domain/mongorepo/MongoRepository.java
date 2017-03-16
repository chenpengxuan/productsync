package com.ymatou.productsync.domain.mongorepo;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.mongodb.DuplicateKeyException;
import com.ymatou.performancemonitorclient.PerformanceStatisticContainer;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoQueryData;
import com.ymatou.productsync.infrastructure.config.datasource.DynamicDataSourceAspect;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.LogWrapper;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.joda.time.DateTime;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * mongo 仓储操作相关
 * Created by chenpengxuan on 2017/2/6.
 */
@Component
public class MongoRepository {
    @Autowired
    private Jongo jongoClient;

    @Autowired
    private LogWrapper logWrapper;

    private final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    /**
     * mongo excutor
     *
     * @param mongoDataList
     * @return
     */
    public boolean excuteMongo(List<MongoData> mongoDataList) throws IllegalArgumentException {
        if (mongoDataList == null || mongoDataList.isEmpty()) {
            throw new IllegalArgumentException("mongoDataList 不能为空");
        }
        List<Boolean> resultList = new ArrayList();
        mongoDataList.stream().forEach(x -> {
            try {
                resultList.add(processMongoData(x));
            } catch (Exception ex) {
                logWrapper.recordErrorLog("processMongoData 操作发生异常,异常原因为：{}", ex.getMessage(), ex);
            }
        });
        return !resultList.contains(false);
    }

    /**
     * mongo 查询
     *
     * @param mongoQueryData
     * @return
     * @throws IllegalArgumentException
     */
    @SuppressWarnings("unchecked")
    public List<Map<String, Object>> queryMongo(MongoQueryData mongoQueryData) throws IllegalArgumentException {
        if (mongoQueryData == null) {
            throw new IllegalArgumentException("mongoData 不能为空");
        }
        if (mongoQueryData.getTableName().isEmpty()) {
            throw new IllegalArgumentException("mongo table name 不能为空");
        }
        MongoCollection collection = jongoClient.getCollection(mongoQueryData.getTableName());
        List<Map<String, Object>> mapList = new ArrayList<>();
        Map<String, Object> tempMap = new HashMap<>();
        Object[] paramList = processQueryCondition(mongoQueryData.getMatchCondition());
        switch (mongoQueryData.getOperationType()) {
            case SELECTSINGLE:
                if (mongoQueryData.getMatchCondition() != null) {
                    tempMap = collection.findOne(MapUtil.makeJsonStringFromMapForJongo(mongoQueryData.getMatchCondition()),paramList).as(HashMap.class);
                } else {
                    tempMap = collection.findOne().as(HashMap.class);
                }
                if (tempMap != null) {
                    mapList.add(tempMap);
                }
                break;
            case SELECTMANY:
                if (mongoQueryData.getMatchCondition() != null) {
                    if(mongoQueryData.getDistinctKey() != null && !mongoQueryData.getDistinctKey().isEmpty()) {
                        mapList = Lists.newArrayList(collection.distinct(mongoQueryData.getDistinctKey()).query(MapUtil.makeJsonStringFromMapForJongo(mongoQueryData.getMatchCondition()),paramList).map(x -> (HashMap<String,Object>)x.toMap()).iterator());
                    }else{
                        mapList = Lists.newArrayList((Iterator<? extends Map<String, Object>>)collection.find(MapUtil.makeJsonStringFromMapForJongo(mongoQueryData.getMatchCondition()),paramList).as(tempMap.getClass()).iterator());
                    }
                } else {
                    mapList = Lists.newArrayList((Iterator<? extends Map<String, Object>>) collection.find().as(tempMap.getClass()).iterator());
                }
                break;
        }
        return mapList;
    }

    /**
     * 处理mongo查询条件
     * @param queryMatchConditionData
     * @return
     * @throws IllegalArgumentException
     */
    private Object[] processQueryCondition(Map<String,Object> queryMatchConditionData){
        List tempResult = new ArrayList();
        if (queryMatchConditionData != null && !queryMatchConditionData.isEmpty()) {
            //针对嵌套Map
            Map<String, Object> parameterizationMap = Maps.filterEntries(queryMatchConditionData, x -> x.getValue() instanceof Map
                    && !Maps.filterKeys((Map<String, Object>) x.getValue(), z -> z.contains("$")).isEmpty());
            if (!parameterizationMap.isEmpty()) {
                parameterizationMap.forEach((x, y) -> {
                    if (y instanceof Map) {
                        Map<String, Object> tempMap = Maps.filterEntries((Map<String, Object>) y, z -> z.getKey().contains("$"));
                        if (!tempMap.isEmpty()) {
                            Map<String, Object> unReplacedMap = (Map<String, Object>) queryMatchConditionData.get(x);
                            tempMap.forEach((k, m) -> {
                                unReplacedMap.replace(k, m, "#");
                                tempResult.add(m);
                            });
                        }
                    }
                });
            }
        }
        return tempResult.toArray();
    }
    /**
     * mongodata 实际操作
     *
     * @param mongoData
     * @return
     */
    private boolean processMongoData(MongoData mongoData) throws IllegalArgumentException {
        if (mongoData == null || mongoData.getTableName().isEmpty()) {
            throw new IllegalArgumentException("mongoData 或者 mongo table name 不能为空");
        }
        boolean result = false;
        try {
            MongoCollection collection = jongoClient.getCollection(mongoData.getTableName());
            //增加定制化性能监控汇报
            result = PerformanceStatisticContainer.addWithReturn(() -> {
                boolean processResult = false;
                Object[] paramList = processQueryCondition(mongoData.getMatchCondition());

                //全局操作，针对所有操作刷update字段
                if(mongoData.getUpdateData() != null && !mongoData.getUpdateData().isEmpty()){
                    List<Map<String,Object>> tempUpdateDataList = mongoData.getUpdateData();
                    tempUpdateDataList.stream().forEach(x -> {
                        x.put("updatetime",new DateTime().getMillis());
                    });
                    mongoData.setUpdateData(tempUpdateDataList);
                }

                switch (mongoData.getOperationType()) {
                    case CREATE:
                        try {

                            processResult = collection.insert(MapUtil.makeObjFromMap(mongoData.getUpdateData())).wasAcknowledged();
                        } catch (DuplicateKeyException ex) {
                            logger.info("{}mongo插入操作发生重复键异常", mongoData.getUpdateData());
                            processResult = true;//如果是因为重复键的插入导致错误,则认为是成功
                        }
                        break;
                    case UPDATE:
                        processResult = !mongoData.getUpdateData().stream().map(xData -> collection.update(MapUtil.makeJsonStringFromMapForJongo(mongoData.getMatchCondition()), paramList)
                                .multi()
                                .with(MapUtil.makeObjFromMap(xData))
                                .wasAcknowledged()).collect(Collectors.toList()).contains(false);
                        break;
                    case UPSERT:
                        try {
                            processResult = !mongoData.getUpdateData().stream().map(xData -> collection.update(MapUtil.makeJsonStringFromMapForJongo(mongoData.getMatchCondition()), paramList)
                                    .upsert()
                                    .with(MapUtil.makeObjFromMap(xData))
                                    .wasAcknowledged()).collect(Collectors.toList()).contains(false);
                        }catch (DuplicateKeyException ex) {
                            logger.info("{}mongo UPSERT操作发生重复键异常", mongoData.getUpdateData());
                            processResult = true;//如果是因为重复键的插入导致错误,则认为是成功
                        }
                        break;
                    case DELETE:
                        processResult = collection.remove(MapUtil.makeJsonStringFromMapForJongo(mongoData.getMatchCondition()), paramList).wasAcknowledged();
                        break;
                    default:
                        throw new IllegalArgumentException("mongo 操作类型不正确");
                }
                return processResult;
            }, "processMongoData_" + mongoData.getOperationType().name() + "_" + mongoData.getTableName(), Constants.APP_ID);
            logger.info("操作mongo信息：mongo表名{},mongo操作类型{},mongo匹配参数为{},mongo同步数据为{},操作结果为{}", mongoData.getTableName(), mongoData.getOperationType().name(), Utils.toJSONString(mongoData.getMatchCondition()), Utils.toJSONString(mongoData.getUpdateData()), result);
        }catch (Exception ex){
            logWrapper.recordErrorLog("processMongoData 发生异常",ex);
        }
        return result;
    }
}

