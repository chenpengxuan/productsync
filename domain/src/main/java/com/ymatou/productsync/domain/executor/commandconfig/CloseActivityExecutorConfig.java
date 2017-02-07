package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.executor.MongoDataCreator;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.*;

import static com.ymatou.productsync.infrastructure.util.Utils.MapFieldToStringArray;

/**
 * Created by chenfei on 2017/2/7.
 * 关闭直播
 */
public class CloseActivityExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;
    @Autowired
    private LiveCommandQuery liveCommandQuery;
    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.CloseActivity;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        ///1.直播数据更新--还有country，flag需要处理
        //设置匹配条件
        Map<String,Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("lid",activityId);
        List<Map<String,Object>> mapList = liveCommandQuery.getActivityInfo(activityId);
         MapFieldToStringArray(mapList,"brands",",");
        //设置要更新的数据
        MongoData liveMongoData = MongoDataCreator.CreateLiveUpdate(matchConditionInfo, mapList);

        ///2.直播商品数据更新


        ///2.商品数据更新

        mongoDataList.add(liveMongoData);
        return mongoDataList;
    }
}
