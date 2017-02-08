package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataCreator;
import com.ymatou.productsync.domain.executor.MongoQueryCreator;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.domain.sqlrepo.LiveCommandQuery;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/8.
 * 商品下架
 */
@Component("productPutoutExecutorConfig")
public class ProductPutoutExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;
    @Autowired
    private LiveCommandQuery liveCommandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ProductPutout;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        if (activityId <= 0) {
            List<Map<String, Object>> deleteProducts = commandQuery.getDeleteProducts(productId);
            if (deleteProducts != null && !deleteProducts.isEmpty()) {
                //更新商品
                MongoData productMd = MongoDataCreator.CreateProductUpdate(MongoQueryCreator.CreateProductId(productId), deleteProducts);
                mongoDataList.add(productMd);
                //删除直播商品
                Map<String, Object> matchConditionInfo = new HashMap();
                matchConditionInfo.put("spid", productId);
                //fixme:matchConditionInfo.put("end",now); <
                MongoData liveProductMd = MongoDataCreator.CreateLiveProductDelete(matchConditionInfo, null);
                mongoDataList.add(liveProductMd);
            }
        } else {
            //fixme: 是否下过单，调订单接口
            //Ymatou.API.Order.Model.Response.YmtAppResponse.GetOrderCountOfProductIdListResponse
            int productOrderCount = 0;
            //商品下过单更新状态，否则下架后删除Mongo数据
            if (productOrderCount > 0) {
                //直播商品更新-istop,status
                List<Map<String, Object>> productTop = commandQuery.getLiveProductTop(productId, activityId);
                mongoDataList.add(MongoDataCreator.CreateLiveProductUpdate(MongoQueryCreator.CreateProductIdAndLiveId(productId, activityId), productTop));
            } else {
                //删除商品相关Mongo数据
                mongoDataList.add(MongoDataCreator.CreateDelete(Constants.ProductDb, MongoQueryCreator.CreateProductId(productId)));
                mongoDataList.add(MongoDataCreator.CreateDelete(Constants.LiveProudctDb, MongoQueryCreator.CreateProductId(productId)));
                mongoDataList.add(MongoDataCreator.CreateDelete(Constants.ProductDescriptionDb, MongoQueryCreator.CreateProductId(productId)));
                mongoDataList.add(MongoDataCreator.CreateDelete(Constants.CatalogDb, MongoQueryCreator.CreateProductId(productId)));
            }
        }
        //fixme: messagebus notify
        //SnapshotService.NotifyMessageBus(productId, "ProductPutout");

        return mongoDataList;
    }
}
