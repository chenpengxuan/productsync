package com.ymatou.productsync.test.domain;

/**
 * Created by zhangyong on 2017/2/22.
 */

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

@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class DeleteProductTest {
    @Autowired
    private TestCommandQuery commandQuery;

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private DeleteProductExecutorConfig deleteProductExecutorConfig;

    /**
     *
     */
    @Test
    public void testDeleteProduct() {
        //从直播中删商品
        long activityId = 157242;
        String productId = "210a0e9a-147f-4f71-b3af-3b97e60fe640";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        boolean checkOk = commandExecutor.executeCommand(req, deleteProductExecutorConfig);
        Asserts.check(checkOk, "从直播中删除商品fail！");
    }

    @Test(expected = BizException.class)
    public void testDeleteProductException() {
        //pc删商品,不带直播id
        String productId2 = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId2);
        boolean checkRet = commandExecutor.executeCommand(req2, deleteProductExecutorConfig);
    }
}
