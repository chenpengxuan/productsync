package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.AddActivityExecutorConfig;
import com.ymatou.productsync.domain.sqlrepo.TestCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.web.ProductSyncApplication;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * unit test
 * Created by zhangyong on 2017/2/22.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class AddActivityTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private AddActivityExecutorConfig addActivityExecutorConfig;

    @Test
    public void testAddActivity() {
        //#1正常添加直播
        long activityId = 157987;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        boolean issuccess = commandExecutor.executeCommand(req, addActivityExecutorConfig);
        Assert.assertTrue(issuccess);
    }

    @Test(expected = BizException.class)
    public void testAddActivityException() {
        //#2sql没有的直播
        SyncByCommandReq req = new SyncByCommandReq();
        long nActivityId = 1572420;
        req.setActivityId(nActivityId);
        commandExecutor.executeCommand(req, addActivityExecutorConfig);
    }
}
