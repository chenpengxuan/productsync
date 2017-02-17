package com.ymatou.productsync.domain.model.mongo;

import org.springframework.util.CollectionUtils;

import java.util.Map;

/**
 * mongo 查询操作抽象
 * Created by chenpengxuan on 2017/2/17.
 */
public class MongoQueryData {
    /**
     * mongo表名
     */
    private String tableName;

    /**
     * mongo 匹配条件
     */
    private Map<String,Object> matchCondition;

    /**
     * mongo操作类型
     */
    private MongoOperationTypeEnum operationType;

    /**
     * 去重标识
     */
    private String distinctKey;

    public String getTableName() {
        return tableName;
    }

    public void setTableName(String tableName) {
        this.tableName = tableName;
    }

    public Map<String, Object> getMatchCondition() {
        return matchCondition;
    }

    public void setMatchCondition(Map<String, Object> matchCondition) {
        this.matchCondition = matchCondition;
    }

    public MongoOperationTypeEnum getOperationType() {
        return operationType;
    }

    public void setOperationType(MongoOperationTypeEnum operationType) {
        this.operationType = operationType;
    }

    public String getDistinctKey() {
        return distinctKey;
    }

    public void setDistinctKey(String distinctKey) {
        this.distinctKey = distinctKey;
    }

    @Override
    public String toString()
    {
        String output= operationType+"   "+tableName+"  ";
        if(!CollectionUtils.isEmpty(matchCondition))
        {
            output += matchCondition.toString();
        }else
        {
            output += "conditions is empty";
        }
        return output;
    }
}
