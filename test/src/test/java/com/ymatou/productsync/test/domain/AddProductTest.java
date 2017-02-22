package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.AddProductExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.AddProductPicsExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.AutoOnShelfProductExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.BatchSetOnShelfExecutorConfig;
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
public class AddProductTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private AddProductExecutorConfig addProductExecutorConfig;

    @Test
    public void testAddProduct() {
        String productId = "3be45de7-1301-42f7-888c-278657e98336";
        int liveId = 157815;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(liveId);
        commandExecutor.executeCommand(req, addProductExecutorConfig);
    }
}
