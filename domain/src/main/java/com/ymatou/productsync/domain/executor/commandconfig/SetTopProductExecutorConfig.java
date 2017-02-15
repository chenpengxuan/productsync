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
import java.util.List;
import java.util.Map;

/**
 * 设置/取消设置橱窗推荐商品
 * Created by zhangyong on 2017/2/15.
 */
@Component("setTopProductExecutorConfig")
public class SetTopProductExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.SetTopProduct;
    }

    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        List<MongoData> mongoDataList = new ArrayList<>();
        List<Map<String, Object>> istop = commandQuery.getProductOnOffTop(productId);
        if (istop == null || istop.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, "getProductOnOffTop 为空");
        }
        mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), istop));
        return mongoDataList;
    }
}
