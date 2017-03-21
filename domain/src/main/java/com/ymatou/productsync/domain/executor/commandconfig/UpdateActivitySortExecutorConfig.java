package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

/**
 * 直播中商品排序
 * Created by chenpengxuan on 2017/2/15.
 */
@Component("updateActivitySortExecutorConfig")
public class UpdateActivitySortExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.UpdateActivitySort;
    }

    private static ProductChangedRange productChangedRange = new ProductChangedRange();

    private static List<String> productChangedTableNameList = new ArrayList<>();

    private static List<String> productIdList = new ArrayList<>();

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        if (activityId <= 0) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), "直播id必须大于0");
        }
        List<Map<String, Object>> sortInfoList = commandQuery.getProductsLiveSort(activityId);
        if (sortInfoList == null || sortInfoList.isEmpty()) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), "getProductsLiveSort为空");
        }

        productIdList.addAll(sortInfoList
                .stream()
                .map(sort ->
                        Optional.ofNullable((String) sort.get("spid")).orElse(""))
                .collect(Collectors.toList())
        );

        List<MongoData> mongoDataList = new ArrayList<>();
        sortInfoList.stream().forEach(sortInfo -> {
            List<Map<String, Object>> tempUpdateData = new ArrayList<>();
            Map<String, Object> tempMap = new HashMap();
            tempMap.put("sort", sortInfo.get("sort"));
            tempUpdateData.add(tempMap);
            mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(sortInfo.get("spid").toString(), activityId), tempUpdateData));
        });

        productChangedTableNameList.add(Constants.LiveProudctDb);

        return mongoDataList;
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {
        productChangedRange.setProductIdList(productIdList);
        productChangedRange.setProductTableRangeList(productChangedTableNameList);
        return productChangedRange;
    }
}
