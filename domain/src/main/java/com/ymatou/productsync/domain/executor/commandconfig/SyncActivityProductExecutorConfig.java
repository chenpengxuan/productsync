package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.MongoData;
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
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();

        List<Map<String, Object>> sqlProducts = commandQuery.getActivityProducts(productId, activityId);
        List<Map<String, Object>> sqlCatalogs = commandQuery.getActivityProductCatalogs(productId, activityId);

        if(sqlProducts != null && !sqlProducts.isEmpty() && sqlCatalogs != null && !sqlCatalogs.isEmpty()) {

            List<Map<String, Object>> tmpCatalogList = new ArrayList<>();
            sqlCatalogs.parallelStream().forEach(data-> {
                Map<String, Object> tmpCatalogMap = new HashMap<>();
                tmpCatalogMap.put("cid", data.get("cid"));
                tmpCatalogMap.put("stock", data.get("stock"));
                tmpCatalogMap.put("price", data.get("price"));

                tmpCatalogList.add(tmpCatalogMap);
            });

            sqlProducts.stream().findFirst().orElse(Collections.emptyMap()).put("catalogs", tmpCatalogList);
            mongoDataList.add(MongoDataBuilder.syncActivityProducts(MongoQueryBuilder.queryProductIdAndActivityId(productId, activityId), sqlProducts));
        }
        return mongoDataList;
    }
}
