package com.ymatou.productsync.domain.executor.commandconfig;

import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.ProductCmdTypeEnum;
import com.ymatou.productsync.domain.model.UpdateData;
import com.ymatou.productsync.domain.sqlrepo.CommandQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

/**
 * 设置、取消橱窗商品场景同步器设定
 * Created by chenpengxuan on 2017/1/23.
 */
@Component("setOnTopExecutorConfig")
public class SetOnTopExecutorConfig implements ExecutorConfig{
    @Autowired
    private CommandQuery commandQuery;

    @Override
    public ProductCmdTypeEnum getCommand() {
        return ProductCmdTypeEnum.SetOnTop;
    }

    @Override
    public UpdateData loadSourceData(int activityId, String productId) {
        List<List<Map<String,Object>>> result = commandQuery.setTopProduct(productId);
        Map<String,List<Map<String,Object>>> tempUpdateData = new Hashtable<>();
        tempUpdateData.put("Products",result.stream().findFirst().orElse(Collections.emptyList()));
        UpdateData updateData = new UpdateData();
        updateData.setUpdateData(tempUpdateData);
        return updateData;
    }
}
