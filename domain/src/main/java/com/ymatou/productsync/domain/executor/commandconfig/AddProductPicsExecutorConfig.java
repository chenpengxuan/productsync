package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

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
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> tempSqlDataList = commandQuery.getProductPictureList(productId);

        if (tempSqlDataList == null || tempSqlDataList.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, "getProductPictureList 为空");
        }

        Object[] pics = tempSqlDataList.parallelStream().map(t -> t.get("pics")).toArray();
        tempSqlDataList.stream().findFirst().orElse(Collections.emptyMap()).replace("pics", pics);
        List<Map<String,Object>> sqlDataList = new ArrayList<>();
        sqlDataList.add(tempSqlDataList.parallelStream().findFirst().orElse(Collections.emptyMap()));
        mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), sqlDataList));
        return mongoDataList;
    }
}
