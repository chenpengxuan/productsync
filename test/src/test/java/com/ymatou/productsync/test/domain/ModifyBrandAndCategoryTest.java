package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.AutoOnShelfProductExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.BatchSetOnShelfExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.ModifyBrandAndCategoryExecutorConfig;
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
public class ModifyBrandAndCategoryTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private ModifyBrandAndCategoryExecutorConfig modifyBrandAndCategoryExecutorConfig;

    @Test
    public void testModifyBrandAndCategory() {
        SyncByCommandReq req = new SyncByCommandReq();
        //        #1现货商品
        List<Map<String, Object>> tproducts = commandQuery.getProduct();
        Map<String, Object> prod = tproducts.stream().findFirst().orElse(Collections.emptyMap());
        req.setProductId(prod.get("sProductId").toString());
        boolean success1 = commandExecutor.executeCommand(req, modifyBrandAndCategoryExecutorConfig);
        Asserts.check(success1, "现货商品修改分类品牌fail！");
    }

    @Test
    public void testModifyBrandAndCategoryLive() {
        SyncByCommandReq req = new SyncByCommandReq();
//        #2直播商品
        List<Map<String, Object>> lproducts = commandQuery.getLiveProduct();
        Map<String, Object> lprod = lproducts.stream().findFirst().orElse(Collections.emptyMap());
        req.setProductId(lprod.get("sProductId").toString());
        boolean success2 = commandExecutor.executeCommand(req, modifyBrandAndCategoryExecutorConfig);
        Asserts.check(success2, "直播商品修改分类品牌fail！");
    }

    @Test(expected = BizException.class)
    public void testModifyBrandAndCategoryException() {
        SyncByCommandReq req = new SyncByCommandReq();
//        #3不存在商品
        req.setProductId("7577884f-8606-4571-ba52-4881e89e660cc");
        commandExecutor.executeCommand(req, modifyBrandAndCategoryExecutorConfig);
    }
}
