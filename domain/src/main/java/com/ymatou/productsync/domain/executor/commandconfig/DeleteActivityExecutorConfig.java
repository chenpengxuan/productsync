package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by chenfei on 2017/2/14.
 * 删除直播
 */
@Component("deleteActivityExecutorConfig")
public class DeleteActivityExecutorConfig implements ExecutorConfig {
    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.DeleteActivity;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        List<MongoData> mongoDataList = new ArrayList<>();

        //直接从mongo中删除直播
        mongoDataList.add(MongoDataBuilder.createDelete(Constants.LiveDb, MongoQueryBuilder.queryLiveId(activityId)));
        return mongoDataList;

    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {
        return null;
    }

}
