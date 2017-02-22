package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 直播中商品排序
 * Created by chenpengxuan on 2017/2/15.
 */
@Component("updateActivitySortExecutorConfig")
public class UpdateActivitySortExecutorConfig implements ExecutorConfig{
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.UpdateActivitySort;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        if(activityId <= 0){
            throw new BizException(ErrorCode.BIZFAIL,"直播id必须大于0");
        }
        List<Map<String,Object>> sortInfoList = commandQuery.getProductsLiveSort(activityId);
        if(sortInfoList == null || sortInfoList.isEmpty()){
            throw new BizException(ErrorCode.BIZFAIL,"getProductsLiveSort为空");
        }
        List<MongoData> mongoDataList = new ArrayList<>();
        sortInfoList.parallelStream().forEach(sortInfo -> {
            List<Map<String,Object>> tempUpdateData = new ArrayList<>();
            Map<String,Object> tempMap = new HashMap();
            tempMap.put("sort",sortInfo.get("sort"));
            tempUpdateData.add(tempMap);
            mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(sortInfo.get("spid").toString(),activityId),tempUpdateData));
        });
        return mongoDataList;
    }
}
