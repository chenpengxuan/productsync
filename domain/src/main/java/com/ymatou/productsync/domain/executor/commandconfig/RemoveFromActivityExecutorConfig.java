package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/15.
 * 商品从直播中移除
 */
@Component("removeFromActivityExecutorConfig")
public class RemoveFromActivityExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Autowired
    private LiveCommandQuery liveCommandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.RemoveFromActivity;
    }

    private static ProductChangedRange productChangedRange = new ProductChangedRange();

    private static List<String> productChangedTableNameList = new ArrayList<>();

    private static List<String> productIdList = new ArrayList<>();

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        List<MongoData> mongoDataList = new ArrayList<>();
        ///1.删掉直播商品关系
        Map<String, Object> liveProductCondition = MongoQueryBuilder.queryProductIdAndLiveId(productId, activityId);
        mongoDataList.add(MongoDataBuilder.createLiveProductDelete(liveProductCondition));

        productChangedTableNameList.add(Constants.LiveProudctDb);

        //2，更新直播品牌
        Map<String, Object> lives = new HashMap();
        List<Map<String, Object>> products = liveCommandQuery.getProductInfoByActivityId(activityId);
        if (products != null && !products.isEmpty()) {
            products.stream().forEach(t -> t.remove("dAddTime"));
            Object[] brands = products.stream().map(t -> t.get("sBrand")).distinct().toArray();
            lives.put("brands", brands);
        }
        else{
            lives.put("brands",null);
        }
        if (!lives.isEmpty()) {
            MongoData liveMd = MongoDataBuilder.createLiveUpdate(MongoQueryBuilder.queryLiveId(activityId), MapUtil.mapToList(lives));
            mongoDataList.add(liveMd);
        }
        //3,更新商品新品时间
        List<Map<String, Object>> productMapList = commandQuery.getDeleteProducts(productId);
        if (productMapList == null || productMapList.isEmpty()) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), this.getCommand() + "-getProductNewTimeByActivityId");
        }
        Map<String, Object> pidCondition = MongoQueryBuilder.queryProductId(productId);
        MongoData productMongoData = MongoDataBuilder.createProductUpdate(pidCondition, productMapList);
        mongoDataList.add(productMongoData);

        productChangedTableNameList.add(Constants.ProductDb);
        productIdList.add(productId);
        return mongoDataList;
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {
        productChangedRange.setProductIdList(productIdList);
        productChangedRange.setProductTableRangeList(productChangedTableNameList);
        return productChangedRange;
    }
}
