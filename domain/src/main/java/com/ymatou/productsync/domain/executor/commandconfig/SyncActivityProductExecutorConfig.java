package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 *  同步活动商品
 * Created by zhujinfeng on 2017/2/9.
 */

@Component("syncActivityProductExecutorConfig")
public class SyncActivityProductExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand(){ return CmdTypeEnum.SyncActivityProduct; }

    @Override
    public List<MongoData> loadSourceData(long productInactivityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();

        List<Map<String, Object>> sqlProducts = commandQuery.getActivityProducts(productInactivityId);
        List<Map<String, Object>> sqlCatalogs = commandQuery.getActivityProductCatalogs(productInactivityId);

        if(sqlProducts != null && !sqlProducts.isEmpty() && sqlCatalogs != null && !sqlCatalogs.isEmpty()) {
            sqlProducts.stream().findFirst().orElse(Collections.emptyMap()).put("catalogs", sqlCatalogs);
            mongoDataList.add(MongoDataBuilder.syncActivityProducts(MongoQueryBuilder.queryProductIdAndActivityId(productInactivityId), sqlProducts));
        }
        return mongoDataList;
    }
}
