package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.CmdTypeEnum;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.MongoDataBuilder;
import com.ymatou.productsync.domain.executor.MongoQueryBuilder;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.infrastructure.util.MapUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.*;

/**
 * Created by zhangyong on 2017/3/23.
 */
@Component("modifyDescTemplateExecutorConfig")
public class ModifyDescTemplateExecutorConfig implements ExecutorConfig {
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public CmdTypeEnum getCommand() {
        return CmdTypeEnum.ModifyDescTemplate;
    }

    @Override
    public List<MongoData> loadSourceData(long activityId, String productId) throws BizException {
        //商品信息
        List<Map<String, Object>> sqlProductDataList = commandQuery.getProductDetailInfo(productId);
        //商品图文描述（2） （Descriptions）
        List<Map<String, Object>> sqlDescriptionsData = commandQuery.getProductDescriptions(productId);

        //前置条件检查
        if (sqlProductDataList == null || sqlProductDataList.isEmpty()) {
            throw new BizException(SyncStatusEnum.BizEXCEPTION.getCode(), "getProductDetailInfo为空");
        }
        List<MongoData> mongoDataList = new ArrayList<>();
        Map<String, Object> tempProductDataMap = sqlProductDataList.stream().findFirst().orElse(Collections.emptyMap());
        Map<String, Object> tempDescMap = new HashMap<>();
        Map<String, Object> tempProductMap = new HashMap<>();

        Map<String, Object> tempDescription = sqlDescriptionsData.stream().findFirst().orElse(Collections.emptyMap());
        MapUtil.mapFieldToStringArray(tempDescription, "notipics", ",");
        MapUtil.mapFieldToStringArray(tempDescription, "intropics", ",");
        tempDescMap.put("notice", tempDescription.get("notice"));
        tempDescMap.put("notipics", tempDescription.get("notipics"));
        tempDescMap.put("intro", tempDescription.get("intro"));
        tempDescMap.put("intropics", tempDescription.get("intropics"));

        List<String> sizepics = null;
        //ProductDetailModel中尺码表图片,卖家上传的放前面,系统导入的放后面
        if (tempDescription.get("sizepics") != null) {
            sizepics = Arrays.asList(tempDescription.get("sizepics").toString().split(","));
            sizepics = new ArrayList<>(sizepics);
            if (tempProductDataMap.get("MeasurePic") != null) {
                sizepics.add(tempProductDataMap.get("MeasurePic").toString());
            }
        }
        tempDescMap.put("sizepics", sizepics);
        tempProductMap.put("sizepics", sizepics);
        mongoDataList.add(MongoDataBuilder.createProductUpdate(MongoQueryBuilder.queryProductId(productId), MapUtil.mapToList(tempProductMap)));
        mongoDataList.add(MongoDataBuilder.createDescriptionsUpdate(MongoQueryBuilder.queryProductId(productId), MapUtil.mapToList(tempDescMap)));
        return mongoDataList;
    }
}
