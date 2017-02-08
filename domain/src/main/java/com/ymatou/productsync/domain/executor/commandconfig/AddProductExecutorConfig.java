package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataCreator;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 添加商品
 * Created by chenpengxuan on 2017/2/8.
 */
@Component("addProductExecutorConfig")
public class AddProductExecutorConfig implements ExecutorConfig{
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.AddProduct;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<Map<String,Object>> sqlDataList = commandQuery.getProductDetailInfo(productId);
        MapUtil.MapFieldToStringArray(sqlDataList,"pics",",");
        List<MongoData> mongoDataList = new ArrayList<>();
        MongoData mongoData = new MongoData();
        Map<String,Object> matchConditionInfo = new HashMap();
        //设置匹配条件
        matchConditionInfo.put("spid",productId);
        mongoDataList.add(MongoDataCreator.CreateProductAdd(matchConditionInfo,sqlDataList));
        return mongoDataList;
    }
}
