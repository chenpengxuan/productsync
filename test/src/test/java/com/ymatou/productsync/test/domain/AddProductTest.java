package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.AddProductExecutorConfig;
import com.ymatou.productsync.domain.sqlrepo.TestCommandQuery;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.web.ProductSyncApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.ArrayList;
import java.util.List;

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
    public void testAddLiveProduct() {
        String productId = "3be45de7-1301-42f7-888c-278657e98336";
        int liveId = 157815;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(liveId);
        commandExecutor.executeCommand(req, addProductExecutorConfig);
    }

    @Test
    public void testAddProduct() {
        String productId = "3be45de7-1301-42f7-888c-278657e98336";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, addProductExecutorConfig);
    }

    @Test
    public void testAddProductList() {
        List<String> productIdList = new ArrayList<>();
        productIdList.add("c1ba2ba5-ee5b-4139-8731-99127715ffb0");
        productIdList.add("ce4fed93-0e50-4595-a8c2-5adf9d99725e");
        productIdList.add("37bd5942-3ccf-4c24-ad2b-f026b18e6794");
        productIdList.add("8d74a622-fb36-456d-8927-5336b0226486");
        productIdList.add("88d079ac-45cf-430c-9f8d-0629bb8f17be");
        productIdList.add("8ffec130-316b-48c2-97ec-70f0a54d7cb5");
        productIdList.forEach(pid -> {
            SyncByCommandReq req = new SyncByCommandReq();
            req.setProductId(pid);
            commandExecutor.executeCommand(req, addProductExecutorConfig);
        });
    }
}
