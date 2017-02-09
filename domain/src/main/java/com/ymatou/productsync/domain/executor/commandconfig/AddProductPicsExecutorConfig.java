package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataCreator;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 同步商品主图 - AddProductPics
 * Created by zhujinfeng on 2017/2/8.
 */
@Component("addProductPicsExecutorConfig")
public class AddProductPicsExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AddProductPics;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();

        List<Map<String, Object>> sqlDataList = commandQuery.getProductPictureList(productId);
        if(sqlDataList != null && !sqlDataList.isEmpty())
        {
            Object[] pics =  sqlDataList.parallelStream().map(t->t.get("pics")).toArray();
            sqlDataList.stream().findFirst().orElse(Collections.emptyMap()).replace("pics",pics);
            Map<String, Object> matchConditionInfo = new HashMap();
            matchConditionInfo.put("spid", productId);
            mongoDataList.add( MongoDataCreator.CreateProductUpdate(matchConditionInfo,sqlDataList));
        }
        return mongoDataList;
    }
}
