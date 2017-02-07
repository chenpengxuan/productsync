package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoOperationTypeEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 设置、取消橱窗商品场景同步器设定
 * Created by chenpengxuan on 2017/1/23.
 */
@Component("setOnTopExecutorConfig")
public class SetOnTopExecutorConfig implements ExecutorConfig{
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.SetOnTop;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<Map<String,Object>> sqlDataList = commandQuery.setTopProduct(productId);
        List<MongoData> mongoDataList = new ArrayList<>();
        MongoData mongoData = new MongoData();
        //设置mongo表名
        mongoData.setTableName("Products");
        Map<String,Object> matchConditionInfo = new HashMap();
        //设置匹配条件
        matchConditionInfo.put("spid",productId);
        //设置操作类型
        mongoData.setOperationType(MongoOperationTypeEnum.UPDATE);
        mongoData.setMatchCondition(matchConditionInfo);
        //设置要更新的数据边界
        mongoData.setUpdateData(sqlDataList);
        mongoDataList.add(mongoData);
        return mongoDataList;
    }


}
