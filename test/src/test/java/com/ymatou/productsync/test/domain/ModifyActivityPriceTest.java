package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.ModifyActivityPriceExecutorConfig;
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
public class ModifyActivityPriceTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private ModifyActivityPriceExecutorConfig modifyActivityPriceExecutorConfig;

    @Test
    public void testModifyActivityPrice() {
//        #1正常修改商品活动价
        SyncByCommandReq req = new SyncByCommandReq();
        List<Map<String, Object>> query = commandQuery.getActivityProduct();
        Map<String, Object> prod = query.stream().findFirst().orElse(Collections.emptyMap());
        req.setProductId(prod.get("sProductId").toString());
        req.setActivityId(Integer.parseInt(prod.get("iProductInActivityId").toString()));
        boolean success1 = commandExecutor.executeCommand(req, modifyActivityPriceExecutorConfig);
        Asserts.check(success1, "测试正常修改商品活动价fail！");
    }

    @Test(expected = BizException.class)
    public void testModifyActivityPriceNpException() {
//        #2测试活动商品没有的情况
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(0);
        boolean success2 = commandExecutor.executeCommand(req, modifyActivityPriceExecutorConfig);
    }

    @Test(expected = BizException.class)
    public void testModifyActivityPriceNcException() {
//        #3测试活动商品规格没有的情况
        SyncByCommandReq req = new SyncByCommandReq();
        List<Map<String, Object>> query3 = commandQuery.getInvalidActivityProduct();
        Map<String, Object> prod1 = query3.stream().findFirst().orElse(Collections.emptyMap());
        req.setActivityId(Integer.parseInt(prod1.get("iProductInActivityId").toString()));
        boolean success3 = commandExecutor.executeCommand(req, modifyActivityPriceExecutorConfig);
    }
}
