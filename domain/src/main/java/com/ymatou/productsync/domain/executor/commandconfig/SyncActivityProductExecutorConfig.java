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
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 *  同步活动商品
 * Created by zhujinfeng on 2017/2/9.
 */

@Component("syncActivityProductExecutorConfig")
public class SyncActivityProductExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;

    @Autowired
    private CatalogStockChangeExecutorConfig catalogStockChangeExecutorConfig;

    @Override
    public CmdTypeEnum getCommand(){ return CmdTypeEnum.SyncActivityProduct; }

    private static ProductChangedRange productChangedRange = new ProductChangedRange();

    private static List<String> productChangedTableNameList = new ArrayList<>();

    private static List<String> productIdList = new ArrayList<>();

    @Override
    public List<MongoData> loadSourceData(long productInactivityId, String productId) throws BizException {
        List<MongoData> mongoDataList = new ArrayList<>();

        List<Map<String, Object>> sqlProducts = commandQuery.getActivityProducts(productInactivityId);
        List<Map<String, Object>> sqlCatalogs = commandQuery.getActivityProductCatalogs(productInactivityId);
        if (sqlProducts == null || sqlProducts.isEmpty()) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), "getActivityProducts 为空");
        }

        if (sqlCatalogs == null || sqlCatalogs.isEmpty()) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), "getActivityProductCatalogs 为空");
        }

        sqlProducts.stream().findFirst().orElse(Collections.emptyMap()).put("catalogs", sqlCatalogs);
        mongoDataList.add(MongoDataBuilder.syncActivityProducts(MongoQueryBuilder.queryProductIdAndActivityId(productInactivityId), sqlProducts));
        mongoDataList.addAll(catalogStockChangeExecutorConfig.loadSourceData(0,productId));

        productChangedTableNameList.add(Constants.ActivityProductDb);
        return mongoDataList;
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {
        ProductChangedRange tempRangeInfo = catalogStockChangeExecutorConfig.getProductChangeRangeInfo();
        productIdList.addAll(tempRangeInfo.getProductIdList());
        productChangedTableNameList.addAll(tempRangeInfo.getProductTableRangeList());
        productChangedRange.setProductIdList(productIdList);
        productChangedRange.setProductTableRangeList(productChangedTableNameList);
        return productChangedRange;
    }
}
