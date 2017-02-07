package com.ymatou.productsync.domain.mongorepo;

import com.alibaba.fastjson.JSON;
import com.ymatou.productsync.domain.model.MongoData;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
            {
                String[] strList = mongoData.getUpdateData().stream().map(x -> makeJsonStringFromMap(x)).toArray(String[]::new);
                return collection.insert(String.join(",",strList)).wasAcknowledged();
            }
            case UPDATE:
                return collection.update(makeJsonStringFromMap(mongoData.getMatchCondition())).multi().with(makeObjFromMap(mongoData.getUpdateData().stream().findFirst().orElse(Collections.emptyMap()))).getN() > 0;
            case UPSERT:
                return collection.update(makeJsonStringFromMap(mongoData.getMatchCondition())).upsert().with(makeObjFromMap(mongoData.getUpdateData().stream().findFirst().orElse(Collections.emptyMap()))).getN() > 0;
            case DELETE:
                return collection.remove(makeJsonStringFromMap(mongoData.getMatchCondition())).getN() > 0;
        }
        return false;
    }

    /**
     * 将map转换成json字符串格式
     *
     * @return
     */
    private String makeJsonStringFromMap(Map<String, Object> map) throws IllegalArgumentException {
        if (map == null || map.isEmpty())
            throw new IllegalArgumentException("mongo 待操作数据不能为空");
        return JSON.toJSONString(map);
    }

    /**
     * 将map转换成obj格式
     *
     * @param map
     * @return
     * @throws IllegalArgumentException
     */
    private Object makeObjFromMap(Map<String, Object> map) throws IllegalArgumentException {
        if (map == null || map.isEmpty())
            throw new IllegalArgumentException("mongo 待操作数据不能为空");
        return JSON.toJSON(map);
    }

    /**
     * 将map转换成obj格式
     *
     * @param mapList
     * @return
     * @throws IllegalArgumentException
     */
    private List<Object> makeObjFromMap(List<Map<String, Object>> mapList) throws IllegalArgumentException {
        if (mapList == null || mapList.isEmpty())
            throw new IllegalArgumentException("mongo 待操作数据不能为空");
        List<Object> tmpObjList = mapList.stream().map(x -> JSON.toJSON(x)).collect(Collectors.toList());
        return mapList.stream().map(x -> JSON.toJSON(x)).collect(Collectors.toList());
    }
}
