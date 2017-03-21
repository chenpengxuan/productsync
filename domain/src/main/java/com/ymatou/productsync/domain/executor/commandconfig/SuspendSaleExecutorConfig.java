package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 暂停销售
 * Created by zhujinfeng on 2017/2/8.
 */
@Component("suspendSaleExecutorConfig")
public class SuspendSaleExecutorConfig implements ExecutorConfig {

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.SuspendSale;
    }

    private static ProductChangedRange productChangedRange = new ProductChangedRange();

    private static List<String> productChangedTableNameList = new ArrayList<>();

    private static List<String> productIdList = new ArrayList<>();

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        productIdList.clear();
        productChangedTableNameList.clear();

        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> sourceData = new ArrayList<>();
        Map<String, Object> map = new HashMap();
        map.put("status", 2);
        map.put("istop", false);
        sourceData.add(map);
        mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(productId, activityId), sourceData));

        productIdList.add(productId);
        productChangedTableNameList.add(Constants.LiveProudctDb);

        productChangedRange.setProductIdList(productIdList);
        productChangedRange.setProductTableRangeList(productChangedTableNameList);
        return mongoDataList;
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {

        return productChangedRange;
    }
}
