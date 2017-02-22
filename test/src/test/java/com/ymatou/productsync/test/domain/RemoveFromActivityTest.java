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
public class RemoveFromActivityTest {
    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private RemoveFromActivityExecutorConfig removeFromActivityExecutorConfig;

    @Test
    public void testRemoveFromActivity() {
        String productId = "79e8a693-359a-4930-a4d3-5ce9c6a52758";
        long activityId = 149338;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check = commandExecutor.executeCommand(req, removeFromActivityExecutorConfig);
        Asserts.check(check, "测试商品移除直播fail！");
    }

    @Test
    public void testRemoveFromActivityException() {
        try {
            //测试一个不存在的直播id
            long activityId2 = 1;
            String productId = "5fbcbc07-16fc-4186-9729-90ba7ba53e57";
            SyncByCommandReq req2 = new SyncByCommandReq();
            req2.setProductId(productId);
            req2.setActivityId(activityId2);
            //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
            boolean check2 = commandExecutor.executeCommand(req2, removeFromActivityExecutorConfig);
            Asserts.check(check2, "测试一个不存在的直播,商品移除直播fail");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }
}
