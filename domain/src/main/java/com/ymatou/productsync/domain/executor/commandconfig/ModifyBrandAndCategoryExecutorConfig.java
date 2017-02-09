package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoOperationTypeEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.apache.tomcat.util.digester.ObjectCreateRule;
import org.assertj.core.util.Strings;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import com.ymatou.productsync.domain.executor.*;

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
            Map<String, Object> matchConditionInfo = new HashMap();
            matchConditionInfo.put("spid", productId);
            mongoDataList.add(MongoDataCreator.CreateProductUpdate(matchConditionInfo, sqlDataList));
        }
        List<Map<String, Object>> liveproducts = commandQuery.getValidLiveByProductId(productId);
        if (liveproducts != null && !liveproducts.isEmpty()) {
            Object[] activityids = liveproducts.stream().map(t -> t.get("iActivityId")).toArray();
            Arrays.stream(activityids).parallel().forEach(t -> {
                Long lid = Long.parseLong(t.toString());
                List<Map<String, Object>> products = commandQuery.getProductInfoByActivityIdForBrandAndCategory(lid);
                Object[] brands = products.stream().distinct().map(x -> Strings.isNullOrEmpty(x.get("sBrand").toString()) ? x.get("sBrandEn") : x.get("sBrand")).toArray();
                Map<String, Object> liveproduct = liveproducts.get(0);
                liveproduct.remove("iActivityId");
                liveproduct.put("brands", brands);// FIXME: 2017/2/8  需要测试(brands == null || brands.Count() == 0) ? null : brands)
                Map<String, Object> matchCon = new HashMap();
                matchCon.put("lid", lid);
                mongoDataList.add(MongoDataCreator.CreateLiveUpdate(matchCon, liveproducts));
                Map<String, Object> matcon = new HashMap();
                matcon.put("lid", lid);
                matcon.put("spid", productId);
                mongoDataList.add(MongoDataCreator.CreateLiveProductUpdate(matcon, sqlDataList));
            });
        }
        return mongoDataList;
    }
}
