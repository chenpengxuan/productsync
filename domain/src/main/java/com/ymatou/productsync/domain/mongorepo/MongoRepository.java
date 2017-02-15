package com.ymatou.productsync.domain.mongorepo;

import com.mongodb.DuplicateKeyException;
import com.ymatou.performancemonitorclient.PerformanceStatisticContainer;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.infrastructure.config.datasource.DynamicDataSourceAspect;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * mongo 仓储操作相关
 * Created by chenpengxuan on 2017/2/6.
 */
@Component
public class MongoRepository{
    @Autowired
    private Jongo jongoClient;

    private final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);

    /**
     * mongo excutor
     *
     * @param mongoDataList
     * @return
     */
    public boolean excuteMongo(List<MongoData> mongoDataList) throws IllegalArgumentException {
        if (mongoDataList == null || mongoDataList.isEmpty())
            throw new IllegalArgumentException("mongoDataList 不能为空");
        List<Boolean> resultList = new ArrayList();
        mongoDataList.stream().forEach(x -> {
            try {
                resultList.add(processMongoData(x));
            } catch (Exception ex) {
                logger.error("processMongoData 操作发生异常,异常原因为：{}", ex.getMessage(), ex);
            }
        });
        return !resultList.contains(false);
    }

    /**
     * mongodata 实际操作
     *
     * @param mongoData
     * @return
     */
    private boolean processMongoData(MongoData mongoData) throws IllegalArgumentException {
        if (mongoData.getTableName().isEmpty())
            throw new IllegalArgumentException("mongo table name 不能为空");
        MongoCollection collection = jongoClient.getCollection(mongoData.getTableName());
        logger.debug("操作mongo信息：mongo表名{},mongo操作类型{},mongo匹配参数为{},mongo同步数据为{}", mongoData.getTableName(), mongoData.getOperationType().name(), Utils.toJSONString(mongoData.getMatchCondition()), Utils.toJSONString(mongoData.getUpdateData()));
        //增加定制化性能监控汇报
        boolean result = PerformanceStatisticContainer.addWithReturn(() -> {
            boolean processResult = false;
            switch (mongoData.getOperationType()) {
                case CREATE:
                    try {
                        processResult = collection.insert(MapUtil.makeObjFromMap(mongoData.getUpdateData())).wasAcknowledged();
                    } catch (DuplicateKeyException ex) {
                        logger.info("{}mongo插入操作发生重复键异常", mongoData.getUpdateData());
                    }
                    break;
                case UPDATE:
                    processResult = collection.update(MapUtil.makeJsonStringFromMap(mongoData.getMatchCondition()))
                            .multi()
                            .with(MapUtil.makeObjFromMap(mongoData.getUpdateData().parallelStream().findFirst().orElse(Collections.emptyMap())))
                            .getN() > 0;
                    break;
                case UPSERT:
                    processResult = collection.update(MapUtil.makeJsonStringFromMap(mongoData.getMatchCondition()))
                            .upsert()
                            .with(MapUtil.makeObjFromMap(mongoData.getUpdateData().parallelStream().findFirst().orElse(Collections.emptyMap())))
                            .getN() > 0;
                    break;
                case DELETE:
                    processResult = collection.remove(MapUtil.makeJsonStringFromMap(mongoData.getMatchCondition())).getN() > 0;
                    break;
                default:
                    throw new IllegalArgumentException("mongo 操作类型不正确");
            }
            return processResult;
        }, "processMongoData_" + mongoData.getOperationType().name() + "_" + mongoData.getTableName(), Constants.APP_ID);
        return result;
    }
}

