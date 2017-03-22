package com.ymatou.productsync.domain.executor.commandconfig;

import com.google.common.collect.Lists;
import com.google.common.primitives.Doubles;
import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.mongo.MongoDataBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.ProductChangedRange;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/9.
 * 商品库存增减,价格修改
 */
@Component("productStockChangeExecutorConfig")
public class ProductStockChangeExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ProductStockChange;
    }

    private static ProductChangedRange productChangedRange = new ProductChangedRange();

    private static List<String> productChangedTableNameList = new ArrayList<>();

    private static List<String> productIdList = new ArrayList<>();

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        productIdList.clear();
        productChangedTableNameList.clear();

        List<MongoData> mongoDataList = new ArrayList<>();
        ///1.规格价格及库存更新
        List<Map<String, Object>> catalogList = commandQuery.getProductCatalogs(productId);
        if (catalogList == null || catalogList.isEmpty()) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), this.getCommand() + "-getProductCatalogs 为空");
        }

        productIdList.add(productId);

        double[] originalPriceList = catalogList.stream().mapToDouble(catalog ->
                Utils.doubleFormat(Double.parseDouble(catalog.get("price") != null ? catalog.get("price").toString() : "0"), 2))
                .toArray();
        double[] newpPriceList = catalogList.stream().mapToDouble(catalog ->
                Utils.doubleFormat(Double.parseDouble(catalog.get("newp") != null ? catalog.get("newp").toString() : "0"), 2))
                .toArray();
        double[] vipPriceList = catalogList.stream().mapToDouble(catalog ->
                Utils.doubleFormat(Double.parseDouble(catalog.get("vip") != null ? catalog.get("vip").toString() : "0"), 2))
                .toArray();

        String maxPrice =
                String.valueOf(Doubles.max(originalPriceList))
                        + ","
                        + String.valueOf(Doubles.max(newpPriceList))
                        + ","
                        + String.valueOf(Doubles.max(vipPriceList));

        String minPrice =
                String.valueOf(Doubles.min(originalPriceList))
                        + ","
                        + String.valueOf(Doubles.min(newpPriceList))
                        + ","
                        + String.valueOf(Doubles.min(vipPriceList));
        catalogList.stream().forEach(catalog -> {
            Map<String, Object> conditions = MongoQueryBuilder.queryProductId(catalog.get("spid") != null ? catalog.get("spid").toString() : "");
            conditions.put("cid", catalog.get("cid"));
            mongoDataList.add(MongoDataBuilder.createUpdate(Constants.CatalogDb, conditions, MapUtil.mapToList(catalog)));
        });

        //更新商品最大最小价格
        List<Map<String, Object>> productCatalog = Lists.newArrayList();
        Map<String, Object> datas = new HashMap<>();
        datas.put("minp", minPrice);
        datas.put("maxp", maxPrice);
        productCatalog.add(datas);
        mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), productCatalog));

        productChangedTableNameList.add(Constants.CatalogDb);
        productChangedTableNameList.add(Constants.ProductDb);

        productChangedRange.setProductIdList(productIdList);
        productChangedRange.setProductTableRangeList(productChangedTableNameList);
        return mongoDataList;
    }

    @Override
    public ProductChangedRange getProductChangeRangeInfo() {

        return productChangedRange;
    }
}
