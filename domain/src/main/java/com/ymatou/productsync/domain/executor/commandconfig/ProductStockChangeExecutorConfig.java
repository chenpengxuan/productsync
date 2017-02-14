package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
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

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) {
        List<MongoData> mongoDataList = new ArrayList<>();
        ///1.规格价格及库存更新
        List<Map<String, Object>> catalogList =  commandQuery.getProductCatalogs(productId);
        if(catalogList!=null && !catalogList.isEmpty()){
            catalogList.parallelStream().forEach(catalog->{
                Map<String,Object> conditions = MongoQueryBuilder.queryProductId(catalog.get("spid").toString());
                conditions.put("cid",catalog.get("cid"));
                mongoDataList.add(MongoDataBuilder.createUpdate(Constants.CatalogDb,conditions, MapUtil.mapToList(catalog)));

            });
        }else{
            throw new BizException(ErrorCode.BIZFAIL,this.getCommand()+"-getProductCatalogs");
        }
        return mongoDataList;
    }
}
