package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/8.
 * 取消商品在直播中置顶
 */
@Component("setOffTopExecutorConfig")
public class SetOffTopExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;
    @Autowired
    private LiveCommandQuery liveCommandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.SetOffTop;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {

        List<MongoData> mongoDataList = new ArrayList<>();
        //直播商品更新-istop
        List<Map<String,Object>> productTop = commandQuery.getLiveProductTop(productId,activityId);
        mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(productId,activityId),productTop));

        //更新直播品牌-brands
        List<Map<String, Object>> lives = liveCommandQuery.getActivityBrand(activityId);
        MapUtil.MapFieldToStringArray(lives, "brands", ",");
        MongoData liveMd = MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(activityId), lives);
        mongoDataList.add(liveMd);
        return mongoDataList;
    }
}
