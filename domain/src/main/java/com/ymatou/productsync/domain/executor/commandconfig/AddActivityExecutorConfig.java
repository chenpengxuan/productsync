package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoOperationTypeEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.session.SessionProperties;
import org.springframework.stereotype.Component;
import java.util.*;

/**
 * 添加直播
 * Created by zhangyong on 2017/2/7.
 */
@Component("addActivityExecutorConfig")
public class AddActivityExecutorConfig implements ExecutorConfig {
    @Autowired
    private LiveCommandQuery commandQuery;

    @Override
    public  CmdTypeEnum getCommand()
    {
        return CmdTypeEnum.AddActivity;
    }

    public List<MongoData> loadSourceData(long activityId,String productId)
    {
        List<Map<String,Object>> sqlDataList=commandQuery.getActivityInfo(activityId);

        List<MongoData> mongoDataList =new ArrayList<>();
        MongoData mongoData=new MongoData();
        mongoData.setTableName(Constants.LiveDb);
        Map<String,Object> matchConditionInfo=new HashMap();
        matchConditionInfo.put("lid",activityId);
        mongoData.setMatchCondition(matchConditionInfo);
        mongoData.setOperationType(MongoOperationTypeEnum.UPSERT);
        mongoData.setUpdateData(sqlDataList);
        mongoDataList.add(mongoData);
        return mongoDataList;
    }


}
