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
 * Created by zhangyong on 2017/2/10.
 */
@Component("modifyActivityPriceExecutorConfig")
public class ModifyActivityPriceExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ModifyActivityPrice;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();

        List<Map<String, Object>> sqlProducts = commandQuery.getActivityProducts(activityId);
        List<Map<String, Object>> sqlCatalogs = commandQuery.getActivityProductCatalogs(activityId);

        if (sqlProducts != null && !sqlProducts.isEmpty() && sqlCatalogs != null && !sqlCatalogs.isEmpty()) {

            List<Map<String, Object>> tmpCatalogList = new ArrayList<>();
            sqlCatalogs.parallelStream().forEach(data -> {
                Map<String, Object> tmpCatalogMap = new HashMap<>();
                tmpCatalogMap.put("cid", data.get("cid"));
                tmpCatalogMap.put("stock", data.get("stock"));
                tmpCatalogMap.put("price", data.get("price"));

                tmpCatalogList.add(tmpCatalogMap);
            });

            sqlProducts.stream().findFirst().orElse(Collections.emptyMap()).put("catalogs", tmpCatalogList);
            mongoDataList.add(MongoDataBuilder.syncActivityProducts(MongoQueryBuilder.queryProductIdAndActivityId(activityId), sqlProducts));
        }
        return mongoDataList;
    }
}
