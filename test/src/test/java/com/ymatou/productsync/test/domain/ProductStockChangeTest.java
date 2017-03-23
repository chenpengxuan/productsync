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
public class ProductStockChangeTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private ProductStockChangeExecutorConfig productStockChangeExecutorConfig;

    @Test
    public void testProductStockChange() {
        String productId = "0a1f92c1-4332-48da-926e-1a84a0e0dc77";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check = commandExecutor.executeCommand(req, productStockChangeExecutorConfig);
        Asserts.check(check, "测试商品库存增减,价格修改fail!");
    }

    @Test(expected = BizException.class)
    public void testProductStockChangeException() {
        //不存在的商品id，exception
        String productId2 = "7577884f-8606-4571-ba52-4881e89e111c";
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId2);
        //List<MongoData> update2 = productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check2 = commandExecutor.executeCommand(req2, productStockChangeExecutorConfig);
        Asserts.check(!check2, "测试不存在的商品id商品库存增减,价格修改fail!");
    }
}
