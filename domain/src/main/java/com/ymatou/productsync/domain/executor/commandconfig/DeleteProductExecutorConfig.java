package com.ymatou.productsync.domain.executor.commandconfig;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
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

            if (deleteProducts == null || deleteProducts.isEmpty()) {
                throw new BizException(ErrorCode.BIZFAIL, this.getCommand() + "-getDeleteProducts为空");
            }

            //更新商品
            MongoData productMd = MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), deleteProducts);
            //删除直播商品
            Map<String, Object> matchConditionInfo = new HashMap();
            matchConditionInfo.put("spid", productId);
            //fixme:matchConditionInfo.put("end",now); <
            MongoData liveProductMd = MongoDataBuilder.createLiveProductDelete(matchConditionInfo, null);
            //删规格
            MongoData catalogMd = MongoDataBuilder.createCatalogDelete(MongoQueryBuilder.queryProductId(productId), Lists.newArrayList());
            mongoDataList.add(productMd);
            mongoDataList.add(liveProductMd);
            //直播品牌不更新
            mongoDataList.add(catalogMd);

        } else { //从直播中删除商品
            //更新直播商品
            List<Map<String, Object>> liveProducts = commandQuery.getLiveProductTime(productId, activityId);
            MongoData liveProductMd = MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductId(productId), liveProducts);
            //更新直播品牌
            Map<String, Object> lives = new HashMap();
            List<Map<String, Object>> products = liveCommandQuery.getProductInfoByActivityId(activityId);
            if (products != null && !products.isEmpty()) {
                products.stream().forEach(t -> t.remove("dAddTime"));
                Object[] brands = products.parallelStream().map(t -> t.get("sBrand")).distinct().toArray();
                lives.put("brands", brands);
            }
            if (!lives.isEmpty()) {
                MongoData liveMd = MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(activityId), MapUtil.mapToList(lives));
                mongoDataList.add(liveMd);
            }
            mongoDataList.add(liveProductMd);
        }
        return mongoDataList;
    }
}
