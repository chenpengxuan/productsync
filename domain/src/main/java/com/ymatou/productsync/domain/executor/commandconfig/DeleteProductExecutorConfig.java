package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/8.
 * 删除商品
 */
@Component("deleteProductExecutorConfig")
public class DeleteProductExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;
    @Autowired
    private LiveCommandQuery liveCommandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.DeleteProduct;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();

        //pc上删除商品
        if (activityId <= 0) {
            List<Map<String, Object>> deleteProducts = commandQuery.getDeleteProducts(productId);
            if (deleteProducts != null && !deleteProducts.isEmpty()) {
                //更新商品
                MongoData productMd = MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), deleteProducts);
                //删除直播商品
                Map<String, Object> matchConditionInfo = new HashMap();
                matchConditionInfo.put("spid", productId);
                //fixme:matchConditionInfo.put("end",now); <
                MongoData liveProductMd = MongoDataBuilder.createLiveProductDelete(matchConditionInfo, null);
                //删规格
                Map<String, Object> actionMap = new HashMap<String, Object>() {{
                    put("action", "-1");
                }};
                List<Map<String, Object>> invalidActions = new ArrayList<Map<String, Object>>() {{
                    add(actionMap);
                }};
                invalidActions.add(actionMap);
                MongoData catalogMd = MongoDataBuilder.createCatalogDelete(MongoQueryBuilder.queryProductId(productId), invalidActions);
                mongoDataList.add(productMd);
                mongoDataList.add(liveProductMd);
                mongoDataList.add(catalogMd);
            }
        } else { //从直播中删除商品
            //更新直播商品
            List<Map<String, Object>> liveProducts = commandQuery.getLiveProductTime(productId, activityId);
            MongoData liveProductMd = MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductId(productId), liveProducts);
            //更新直播品牌
            List<Map<String, Object>> lives = liveCommandQuery.getActivityBrand(activityId);
            MapUtil.MapFieldToStringArray(lives, "brands", ",");
            MongoData liveMd = MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(activityId), lives);
            mongoDataList.add(liveProductMd);
            mongoDataList.add(liveMd);
        }
        //fixme: messagebus notify
        return mongoDataList;
    }
}
