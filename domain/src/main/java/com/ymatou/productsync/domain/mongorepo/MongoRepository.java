package com.ymatou.productsync.domain.mongorepo;

import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
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
public class MongoRepository {
    @Autowired
    private Jongo jongoClient;

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
        mongoDataList.stream().forEach(x -> resultList.add(processMongoData(x)));
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
        switch (mongoData.getOperationType()) {
            case CREATE:
                return collection.insert(MapUtil.makeJsonStringFromMap(mongoData.getUpdateData())).wasAcknowledged();
            case UPDATE:
                return collection.update(MapUtil.makeJsonStringFromMap(mongoData.getMatchCondition())).multi().with(MapUtil.makeObjFromMap(mongoData.getUpdateData().stream().findFirst().orElse(Collections.emptyMap()))).getN() > 0;
            case UPSERT:
                return collection.update(MapUtil.makeJsonStringFromMap(mongoData.getMatchCondition())).upsert().with(MapUtil.makeObjFromMap(mongoData.getUpdateData().stream().findFirst().orElse(Collections.emptyMap()))).getN() > 0;
            case DELETE:
                return collection.remove(MapUtil.makeJsonStringFromMap(mongoData.getMatchCondition())).getN() > 0;
        }
        return false;
    }
}
