package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
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
            throw new BizException(ErrorCode.BIZFAIL, "getProductBrandAndCategory为空");
        }
        List<Map<String, Object>> liveproducts = commandQuery.getValidLiveByProductId(productId);
        if (liveproducts != null && !liveproducts.isEmpty()) {
            Object[] activityids = liveproducts.parallelStream().map(t -> t.get("iActivityId")).toArray();
            Arrays.stream(activityids).parallel().forEach(t -> {
                Long lid = Long.parseLong(t.toString());
                List<Map<String, Object>> products = commandQuery.getProductInfoByActivityIdForBrandAndCategory(lid);
                Object[] brands = products.parallelStream().distinct().map(x -> Strings.isNullOrEmpty(x.get("sBrand").toString()) ? x.get("sBrandEn") : x.get("sBrand")).toArray();
                Map<String, Object> liveproduct = liveproducts.parallelStream().findFirst().orElse(Collections.emptyMap());
                liveproduct.remove("iActivityId");
                liveproduct.put("brands", brands);// FIXME: 2017/2/8  需要测试(brands == null || brands.Count() == 0) ? null : brands)
                mongoDataList.add(MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(lid), liveproducts));
                mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(productId, lid), sqlDataList));
            });
        }
        return mongoDataList;
    }
}
