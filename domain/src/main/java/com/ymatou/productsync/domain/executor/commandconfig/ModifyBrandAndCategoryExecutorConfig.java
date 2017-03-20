package com.ymatou.productsync.domain.executor.commandconfig;

import com.google.common.collect.Lists;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
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
        List<Map<String, Object>> liveproducts = commandQuery.getValidLiveByProductId(productId);
        if (liveproducts != null && !liveproducts.isEmpty()) {
            Lists.newArrayList(liveproducts.stream().map(t -> t.get("lid")).iterator()).stream().forEach(t -> {
                Long lid = Long.parseLong(t.toString());
                List<Map<String, Object>> products = commandQuery.getProductInfoByActivityIdForBrandAndCategory(lid);
                Object[] brands = products.stream().distinct().map(x -> Strings.isNullOrEmpty(x.get("sBrand") != null ? x.get("sBrand").toString() : "") ? x.get("sBrandEn") : x.get("sBrand")).toArray();
                Map<String, Object> liveproduct = liveproducts.stream().findFirst().orElse(Collections.emptyMap());
                liveproduct.remove("lid");
                liveproduct.put("brands", brands);// FIXME: 2017/2/8  需要测试(brands == null || brands.Count() == 0) ? null : brands)
                mongoDataList.add(MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(lid), Arrays.asList(liveproduct)));
                mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(productId, lid), sqlDataList));
            });
        }
        return mongoDataList;
    }
}
