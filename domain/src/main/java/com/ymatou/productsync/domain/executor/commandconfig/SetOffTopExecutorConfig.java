package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/8.
 * 取消商品在直播中置顶 chenfei
 */
@Component("setOffTopExecutorConfig")
public class SetOffTopExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.SetOffTop;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {

        List<MongoData> mongoDataList = new ArrayList<>();
        //直播商品更新-istop
        List<Map<String, Object>> productTop = commandQuery.getLiveProductTop(productId, activityId);
        if (productTop == null || productTop.isEmpty()) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), this.getCommand() + "-getLiveProductTop 为空");
        }
        mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(productId, activityId), productTop));

        return mongoDataList;
    }
}
