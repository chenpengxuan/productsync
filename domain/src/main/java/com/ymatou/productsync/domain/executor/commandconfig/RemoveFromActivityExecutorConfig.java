package com.ymatou.productsync.domain.executor.commandconfig;

import com.google.common.collect.Lists;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * Created by chenfei on 2017/2/15.
 * 商品从直播中移除
 */
public class RemoveFromActivityExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Autowired
    private LiveCommandQuery liveCommandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.RemoveFromActivity;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        List<MongoData> mongoDataList = new ArrayList<>();
        ///1.删掉直播商品关系
        Map<String, Object> liveProductCondition = MongoQueryBuilder.queryProductIdAndLiveId(productId, activityId);
        mongoDataList.add(MongoDataBuilder.createLiveProductDelete(liveProductCondition, Lists.newArrayList()));
        //2，更新直播品牌
        Map<String, Object> lives = new HashMap();
        List<Map<String, Object>> products = liveCommandQuery.getProductInfoByActivityId(activityId);
        if (products == null || products.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, this.getCommand() + "-getProductInfoByActivityId");
        }
        if (products != null && !products.isEmpty()) {
            products.stream().forEach(t -> t.remove("dAddTime"));
            Object[] brands = products.parallelStream().map(t -> t.get("sBrand")).distinct().toArray();
            lives.put("brands", brands);
        }
        if (!lives.isEmpty()) {
            MongoData liveMd = MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(activityId), MapUtil.mapToList(lives));
            mongoDataList.add(liveMd);
        }
        //3,更新商品新品时间
        List<Map<String, Object>> productMapList = commandQuery.getDeleteProducts(productId);
        if (productMapList == null || productMapList.isEmpty()) {
            throw new BizException(ErrorCode.BIZFAIL, this.getCommand() + "-getProductNewTimeByActivityId");
        }
        Map<String, Object> pidCondition = MongoQueryBuilder.queryProductId(productId);
        MongoData productMongoData = MongoDataBuilder.createProductUpdate(pidCondition, productMapList);
        mongoDataList.add(productMongoData);

        return mongoDataList;
    }
}
