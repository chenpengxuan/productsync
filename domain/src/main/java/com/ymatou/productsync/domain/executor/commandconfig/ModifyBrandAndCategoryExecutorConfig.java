package com.ymatou.productsync.domain.executor.commandconfig;

import com.google.common.collect.Lists;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 修改分类品牌
 * Created by zhangyong on 2017/2/8.
 */
@Component("modifyBrandAndCategoryExecutorConfig")
public class ModifyBrandAndCategoryExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ModifyBrandAndCategory;
    }


    public List<MongoData> loadSourceData(long activityId, String productId) {

        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> sqlDataList = commandQuery.getProductBrandAndCategory(productId);
        if (sqlDataList != null && !sqlDataList.isEmpty()) {
            mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), sqlDataList));
        } else {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), "getProductBrandAndCategory为空");
        }

        List<Map<String, Object>> liveProducts = commandQuery.getValidLiveByProductId(productId);
        if (liveProducts != null && !liveProducts.isEmpty()) {
            Lists.newArrayList(liveProducts.stream().map(t -> t.get("lid")).iterator()).stream().forEach(t -> {
                Long lid = Long.parseLong(t.toString());
                List<Map<String, Object>> products = commandQuery.getProductInfoByActivityIdForBrandAndCategory(lid);
                Object[] brands = products.stream().distinct().map(x -> Strings.isNullOrEmpty(x.get("sBrand") != null ? x.get("sBrand").toString() : "") ? x.get("sBrandEn") : x.get("sBrand")).toArray();
                Map<String, Object> liveProduct = liveProducts.stream().findFirst().orElse(Collections.emptyMap());
                liveProduct.remove("lid");
                liveProduct.put("brands", brands);
                mongoDataList.add(MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(lid), Arrays.asList(liveProduct)));
                mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(productId, lid), sqlDataList));
            });
        }

        return mongoDataList;
    }
}
