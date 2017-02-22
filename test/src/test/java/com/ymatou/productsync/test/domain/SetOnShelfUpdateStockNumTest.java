package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.*;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.TestCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.web.ProductSyncApplication;
import org.apache.http.util.Asserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * Created by zhangyong on 2017/2/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class SetOnShelfUpdateStockNumTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private SetOnShelfUpdateStockNumExecutorConfig setOnShelfUpdateStockNumExecutorConfig;

    @Test
    public void testSetOnShelfUpdateStockNum() {
//        #1正常批量上架商品【商品售罄】
        List<Map<String, Object>> query = commandQuery.getLiveProduct();
        Map<String, Object> prod = query.stream().findFirst().orElse(Collections.emptyMap());
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(prod.get("sProductId").toString());
        req.setActivityId(Integer.parseInt(prod.get("iActivityId").toString()));
        boolean success1 = commandExecutor.executeCommand(req, setOnShelfUpdateStockNumExecutorConfig);
        Asserts.check(success1, "测试正常批量上架商品【商品售罄】fail！");


//        long activityId = 157589;
//        String productId = "9022e96f-f273-47d0-a2a4-de2085235b43";
//        SyncByCommandReq req = new SyncByCommandReq();
//        req.setActivityId(activityId);
//        req.setProductId(productId);
//        boolean success1 = commandExecutor.executeCommand(req, setOnShelfUpdateStockNumExecutorConfig);
//        Asserts.check(success1, "测试正常批量上架商品【商品售罄】fail！");
    }
}
