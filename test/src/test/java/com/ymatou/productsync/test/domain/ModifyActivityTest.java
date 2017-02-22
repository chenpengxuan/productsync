package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.ModifyActivityExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.ModifyActivityPriceExecutorConfig;
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
public class ModifyActivityTest {

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private ModifyActivityExecutorConfig modifyActivityExecutorConfig;

    @Test
    public void testModifyActivity() {
        long activityId = 157987;
        //String productId = "e06c1578-e88e-43c1-a763-519e9fb60701";
        SyncByCommandReq req = new SyncByCommandReq();
        //req.setProductId(productId);
        req.setActivityId(activityId);
        boolean isOk = commandExecutor.executeCommand(req, modifyActivityExecutorConfig);
        Asserts.check(isOk, "修改直播fail！");
    }

    @Test
    public void testModifyActivityException() {
        //无效的直播Id
        try {
            long activityId2 = 0;
            SyncByCommandReq req2 = new SyncByCommandReq();
            req2.setActivityId(activityId2);
            boolean isOk2 = commandExecutor.executeCommand(req2, modifyActivityExecutorConfig);
            Asserts.check(!isOk2, "");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }

    @Test
    public void testModifyActivityProduct() {
        //有直播商品的直播，要更新直播商品、商品信息
        long activityId3 = 157305;
        SyncByCommandReq req3 = new SyncByCommandReq();
        req3.setActivityId(activityId3);
        boolean isOk3 = commandExecutor.executeCommand(req3, modifyActivityExecutorConfig);
        Asserts.check(isOk3, "修改直播fail！");
    }
}
