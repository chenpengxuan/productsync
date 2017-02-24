package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.SetTopProductExecutorConfig;
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
 * Created by zhangyong on 2017/2/21.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class SetTopProductTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private SetTopProductExecutorConfig setTopProductExecutorConfig;

    @Test
    //#1正常设置橱窗推荐商品
    public void testSetTopProduct() {
        SyncByCommandReq req = new SyncByCommandReq();
        List<Map<String, Object>> tproducts = commandQuery.getTopProduct();
        Map<String, Object> prod = tproducts.stream().findFirst().orElse(Collections.emptyMap());
        req.setProductId(prod.get("sProductId").toString());
        boolean success1 = commandExecutor.executeCommand(req, setTopProductExecutorConfig);
        Asserts.check(success1, "正常设置橱窗推荐商品fail！");
    }

    @Test
    //#2正常取消设置橱窗推荐产品
    public void testUnSetTopProduct() {
        SyncByCommandReq req = new SyncByCommandReq();
        List<Map<String, Object>> ntprods = commandQuery.getNotTopProduct();
        Map<String, Object> nprod = ntprods.stream().findFirst().orElse(Collections.emptyMap());
        req.setProductId(nprod.get("sProductId").toString());
        boolean success2 = commandExecutor.executeCommand(req, setTopProductExecutorConfig);
        Asserts.check(success2, "正常取消设置橱窗推荐产品fail！");
    }

    @Test(expected = BizException.class)
    //#3操作不存在的商品
    public void testSetTopProductExeception() {
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId("edc21ac6-5fc9-494c-9f36-110b841f75a00");
        boolean success3 = commandExecutor.executeCommand(req, setTopProductExecutorConfig);
    }
}
