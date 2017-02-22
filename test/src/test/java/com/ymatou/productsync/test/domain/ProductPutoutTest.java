package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.ProductPutoutExecutorConfig;
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

/**
 * Created by zhangyong on 2017/2/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class ProductPutoutTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private ProductPutoutExecutorConfig productPutoutExecutorConfig;

    /**
     *
     */
    @Test
    public void testProductPutout() {
        //带直播id场景
        long activityId = 3152;
        String productId = "f68f94f6-898a-4df7-823a-f187c0b62db3";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        //List<MongoData> update= productPutoutExecutorConfig.loadSourceData(activityId,productId);
        boolean check = commandExecutor.executeCommand(req, productPutoutExecutorConfig);
        Asserts.check(check, "");
    }

    @Test
    public void testProductPutoutException() {
        //不带直播id场景
        try {
            String productId2 = "cde180d5-b57c-4e2c-ae1c-db39adcb8c4b";
            SyncByCommandReq req2 = new SyncByCommandReq();
            req2.setProductId(productId2);
            //List<MongoData> update2= productPutoutExecutorConfig.loadSourceData(0,productId);
            boolean checkOk = commandExecutor.executeCommand(req2, productPutoutExecutorConfig);
            Asserts.check(checkOk, "");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }
}
