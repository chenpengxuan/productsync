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
public class SetOffTopConfig {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private SetOffTopExecutorConfig setOffTopExecutorConfig;

    @Autowired
    private SetOnTopExecutorConfig setOnTopExecutorConfig;

    /**
     * setOnTopExecutorConfig
     */
    @Test
    public void testSetOffTop() {
        //测试取消置顶
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        boolean checkOk = commandExecutor.executeCommand(req, setOffTopExecutorConfig);
        Asserts.check(checkOk, "测试取消置顶fail!");
    }

    @Test
    public void testSetOffTopException() {
        //测试置顶
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId);
        req2.setActivityId(activityId);
        boolean checkOk2 = commandExecutor.executeCommand(req2, setOnTopExecutorConfig);
        Asserts.check(checkOk2, "测试置顶fail");
    }
}
