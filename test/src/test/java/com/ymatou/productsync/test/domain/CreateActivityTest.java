package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.AddProductPicsExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.AutoOnShelfProductExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.BatchSetOnShelfExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.CreateActivityExecutorConfig;
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
public class CreateActivityTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private CreateActivityExecutorConfig createActivityExecutorConfig;

    @Test
    public void testCreateActivity() {
        SyncByCommandReq req = new SyncByCommandReq();
        //#1正常创建直播
        long activityId = 157242;

        req.setActivityId(activityId);
        boolean issuccess = commandExecutor.executeCommand(req, createActivityExecutorConfig);
        Asserts.check(issuccess, "正常创建直播fail！");
    }

    @Test
    public void testCreateActivityException() {
        //#2sql没有的直播
        SyncByCommandReq req = new SyncByCommandReq();
        long nActivityId = 1572420;
        req.setActivityId(nActivityId);
        try {
            commandExecutor.executeCommand(req, createActivityExecutorConfig);
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }
}
