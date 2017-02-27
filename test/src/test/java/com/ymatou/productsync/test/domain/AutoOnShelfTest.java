package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
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
public class AutoOnShelfTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private AutoOnShelfProductExecutorConfig autoOnShelfProductExecutorConfig;

    @Test
    public void testAutoRefreshProduct() {
//        #1商品自动上架
        List<Map<String, Object>> tproducts = commandQuery.getProduct();
        Map<String, Object> prod = tproducts.stream().findFirst().orElse(Collections.emptyMap());
        SyncByCommandReq req = new SyncByCommandReq();
        //req.setProductId(prod.get("sProductId").toString());
        req.setProductId("c1ba2ba5-ee5b-4139-8731-99127715ffb0");
        boolean success = commandExecutor.executeCommand(req, autoOnShelfProductExecutorConfig);
        Asserts.check(success, "测试商品自动上架fail！");
    }
}
