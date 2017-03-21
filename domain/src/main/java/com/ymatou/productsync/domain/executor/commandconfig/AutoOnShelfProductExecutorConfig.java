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

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyong on 2017/2/9.
 */
@Component("autoOnShelfProductExecutorConfig")
public class AutoOnShelfProductExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AutoOnShelf;
    }

    private static ProductChangedRange productChangedRange = new ProductChangedRange();

    private static List<String> productChangedTableNameList = new ArrayList<>();

    private static List<String> productIdList = new ArrayList<>();

    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> sqlDataList = commandQuery.getProductTime(productId);
        if (sqlDataList == null || sqlDataList.isEmpty()) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), "getProductTime 为空");
        }
        mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), sqlDataList));

        productChangedTableNameList.add(Constants.ProductDb);
        productIdList.add(productId);

        return mongoDataList;
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {
        productChangedRange.setProductIdList(productIdList);
        productChangedRange.setProductTableRangeList(productChangedTableNameList);
        return productChangedRange;
    }
}

