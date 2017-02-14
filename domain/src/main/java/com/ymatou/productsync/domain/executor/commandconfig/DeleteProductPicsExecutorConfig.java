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

import java.util.*;

/**
 * 同步商品主图列表 - DeleteProductPics
 * Created by zhujinfeng on 2017/2/8.
 */
@Component("deleteProductPicsExecutorConfig")
public class DeleteProductPicsExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.DeleteProductPics;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> sqlDataList = commandQuery.getProductPictureList(productId);

        if (sqlDataList == null || sqlDataList.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, "getProductPictureList 为空");
        }

        Object[] pics = sqlDataList.parallelStream().map(t -> t.get("pics")).toArray();
        sqlDataList.stream().findFirst().orElse(Collections.emptyMap()).replace("pics", pics);
        mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), sqlDataList));

        return mongoDataList;
    }
}
