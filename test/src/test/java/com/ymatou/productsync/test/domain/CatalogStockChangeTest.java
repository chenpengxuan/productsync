package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.CatalogStockChangeExecutorConfig;
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
public class CatalogStockChangeTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private CatalogStockChangeExecutorConfig catalogStockChangeExecutorConfig;

    @Test
    public void testCatalogStockChange() {
        SyncByCommandReq req = new SyncByCommandReq();
//        #1 正常商品规格库存测试
        List<Map<String, Object>> tproducts = commandQuery.getProduct();
        Map<String, Object> prod = tproducts.stream().findFirst().orElse(Collections.emptyMap());
        req.setProductId(prod.get("sProductId") != null ? prod.get("sProductId").toString():"");
        boolean success = commandExecutor.executeCommand(req, catalogStockChangeExecutorConfig);
        Asserts.check(success, "正常商品规格库存测试fail!");
    }

    @Test
    public void testCatalogStockChangeException() {
        SyncByCommandReq req = new SyncByCommandReq();
//        #2 超多规格库存测试
        req.setProductId("520f6853-08c3-42be-8e3b-ec0b171ae6c1");
        try {
            boolean success1 = commandExecutor.executeCommand(req, catalogStockChangeExecutorConfig);
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }
}
