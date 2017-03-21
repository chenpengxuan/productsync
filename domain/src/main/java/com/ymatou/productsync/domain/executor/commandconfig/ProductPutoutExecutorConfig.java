package com.ymatou.productsync.domain.executor.commandconfig;

import com.google.common.collect.Lists;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.LogWrapper;
import com.ymatou.sellerquery.facade.OrderProductInfoFacade;
import com.ymatou.sellerquery.facade.model.req.GetOrderProductAmountInfosReq;
import com.ymatou.sellerquery.facade.model.resp.GetOrderProductAmountInfosResp;
import com.ymatou.sellerquery.facade.model.vo.OrderProductAmountInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.*;

/**
 * Created by chenfei on 2017/2/8.
 * 商品下架
 */
@Component("productPutoutExecutorConfig")
public class ProductPutoutExecutorConfig implements ExecutorConfig {

    @Autowired
    private CommandQuery commandQuery;

    @Autowired
    private LogWrapper logWrapper;

    @Resource
    private OrderProductInfoFacade orderProductInfoFacade;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ProductPutout;
    }

    private static ProductChangedRange productChangedRange = new ProductChangedRange();

    private static List<String> productChangedTableNameList = new ArrayList<>();

    private static List<String> productIdList = new ArrayList<>();

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        if (activityId <= 0) {
            List<Map<String, Object>> deleteProducts = commandQuery.getDeleteProducts(productId);
            if (deleteProducts == null || deleteProducts.isEmpty()) {
                throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), this.getCommand() + "-getDeleteProducts 为空");
            }

            //更新商品
            MongoData productMd = MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), deleteProducts);
            mongoDataList.add(productMd);

            //删除直播商品
            Map<String, Object> matchConditionInfo = new HashMap();
            matchConditionInfo.put("spid", productId);
            Map<String, Object> tempMap = new HashMap<>();
            tempMap.put("$gte", new Date());
            matchConditionInfo.put("end", tempMap);
            MongoData liveProductMd = MongoDataBuilder.createLiveProductDelete(matchConditionInfo);
            mongoDataList.add(liveProductMd);

            productChangedTableNameList.add(Constants.ProductDb);
            productChangedTableNameList.add(Constants.LiveProudctDb);

        } else {
            //是否下过单，调订单接口
            int productOrderCount = 0;
            List<Map<String, Object>> userIdSource = commandQuery.getProductUser(productId);
            if (userIdSource == null || userIdSource.isEmpty()) {
                throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), this.getCommand() + "-getProductUser 为空");
            }
            Map<String, Object> sellerMap = userIdSource.stream().findFirst().orElse(Collections.emptyMap());
            long sellerId = Long.parseLong(sellerMap.get("userId").toString());
            GetOrderProductAmountInfosReq getOrderAmountInfosRequest = new GetOrderProductAmountInfosReq();
            getOrderAmountInfosRequest.setProductIds(Lists.newArrayList(productId));
            getOrderAmountInfosRequest.setSellerId(sellerId);
            //调订单接口，业务对下架无太大影响，可以忽略异常
            try {
                GetOrderProductAmountInfosResp respOrderAmount = orderProductInfoFacade.getOrderProductAmountInfos(getOrderAmountInfosRequest);
                if (respOrderAmount != null && respOrderAmount.isSuccess()) {
                    HashMap<String, OrderProductAmountInfo> orderAmountMap = respOrderAmount.getAmountInfos();
                    if (orderAmountMap.get(productId) != null)
                        productOrderCount = orderAmountMap.get(productId).getPaid();
                }
            } catch (Exception ex) {
                logWrapper.recordErrorLog("商品下架调用订单数接口getOrderProductAmountInfos异常", ex);
            }

            //商品下过单更新状态，否则下架后删除Mongo数据
            if (productOrderCount > 0) {
                //直播商品更新-istop,status
                List<Map<String, Object>> productTop = commandQuery.getLiveProductTop(productId, activityId);
                mongoDataList.add(MongoDataBuilder.createLiveProductUpdate(MongoQueryBuilder.queryProductIdAndLiveId(productId, activityId), productTop));

                productChangedTableNameList.add(Constants.LiveProudctDb);
            } else {
                //删除商品相关Mongo数据
                mongoDataList.add(MongoDataBuilder.createDelete(Constants.ProductDb, MongoQueryBuilder.queryProductId(productId)));
                mongoDataList.add(MongoDataBuilder.createDelete(Constants.LiveProudctDb, MongoQueryBuilder.queryProductId(productId)));
                mongoDataList.add(MongoDataBuilder.createDelete(Constants.ProductDescriptionDb, MongoQueryBuilder.queryProductId(productId)));
                mongoDataList.add(MongoDataBuilder.createDelete(Constants.CatalogDb, MongoQueryBuilder.queryProductId(productId)));

                productChangedTableNameList.add(Constants.ProductDb);
                productChangedTableNameList.add(Constants.LiveProudctDb);
                productChangedTableNameList.add(Constants.CatalogDb);
            }

        }

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
