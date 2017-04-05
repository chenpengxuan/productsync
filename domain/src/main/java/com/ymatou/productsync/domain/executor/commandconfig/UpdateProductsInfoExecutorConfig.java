package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * 修改商品描述与图片信息
 * Created by chenpengxuan on 2017/2/15.
 */
@Component("updateProductsInfoExecutorConfig")
public class UpdateProductsInfoExecutorConfig implements ExecutorConfig{
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.UpdateProductsInfo;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        if(productId == null || productId.isEmpty()){
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(),"productId不能为空");
        }
        //商品图文描述
        List<Map<String, Object>> sqlProductDescDataList = commandQuery.getProductDescInfo(productId);
        if(sqlProductDescDataList == null || sqlProductDescDataList.isEmpty()){
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(),"getProductDescInfo为空");
        }
        Map<String, Object> tempMap = new HashMap<>();
        tempMap.putAll(sqlProductDescDataList.stream().findFirst().orElse(Collections.emptyMap()));
        tempMap.remove("pic");
        tempMap.put("pics", sqlProductDescDataList.stream().map(x -> x.get("pic")).toArray());
        sqlProductDescDataList.clear();
        sqlProductDescDataList.add(tempMap);
        List<MongoData> mongoDataList = new ArrayList<>();
        mongoDataList.add(MongoDataBuilder.createProductDescUpsert(MongoQueryBuilder.queryProductId(productId),sqlProductDescDataList));
        return mongoDataList;
    }
}
