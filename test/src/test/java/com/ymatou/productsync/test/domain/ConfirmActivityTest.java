package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.*;
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
public class ConfirmActivityTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private ConfirmActivityExecutorConfig confirmActivityExecutorConfig;

    @Test
    public void testConfirmActivity() {
        //#1正常确认直播
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        boolean issucess = commandExecutor.executeCommand(req, confirmActivityExecutorConfig);
        Asserts.check(issucess, "测试确认直播车fail！");
    }

    @Test(expected = BizException.class)
    public void testConfirmActivityException() {
        SyncByCommandReq req = new SyncByCommandReq();
        //#2sql没有的直播
        long nActivityId = 1572420;
        req.setActivityId(nActivityId);
        commandExecutor.executeCommand(req, confirmActivityExecutorConfig);
    }
}
