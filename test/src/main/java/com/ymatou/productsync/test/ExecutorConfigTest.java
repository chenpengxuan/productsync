package com.ymatou.productsync.test;

import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.*;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.web.ProductSyncApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * 场景业务指令器test
 * Created by chenpengxuan on 2017/1/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class ExecutorConfigTest {
    @Autowired
    private SetOnTopExecutorConfig setOnTopExecutorConfig;

    @Autowired
    private AddActivityExecutorConfig addActivityExecutorConfig;

    @Autowired
    private ConfirmActivityExecutorConfig confirmActivityExecutorConfig;

    @Autowired
    private CreateActivityExecutorConfig createActivityExecutorConfig;

    @Autowired
    private ModifyBrandAndCategoryExecutorConfig modifyBrandAndCategoryExecutorConfig;

    @Autowired
    private AddProductExecutorConfig addProductExecutorConfig;

    @Autowired
    private AddProductPicsExecutorConfig addProductPicsExecutorConfig;

    @Autowired
    private DeleteProductPicsExecutorConfig deleteProductPicsExecutorConfig;

    @Autowired
    private SuspendSaleExecutorConfig suspendSaleExecutorConfig;

    @Autowired
    private AutoOnShelfProductExecutorConfig autoOnShelfProductExecutorConfig;

    @Autowired
    private CatalogStockChangeExecutorConfig catalogStockChangeExecutorConfig;

    @Autowired
    private  ModifyActivityExecutorConfig modifyActivityExecutorConfig ;

    @Autowired
    private  SetOffTopExecutorConfig setOffTopExecutorConfig ;

    @Autowired
    private  DeleteProductExecutorConfig deleteProductExecutorConfig ;

    @Autowired
    private SyncActivityProductExecutorConfig syncActivityProductExecutorConfig;

    @Autowired
    private CommandExecutor commandExecutor;

    @Test
    public void testSetOnTopExecutorConfig() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executorCommand(req, setOnTopExecutorConfig);
    }

    @Test
    public void testAddActivity() {
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        commandExecutor.executorCommand(req, addActivityExecutorConfig);
    }

    @Test
    public void testConfirmActivity() {
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        commandExecutor.executorCommand(req, confirmActivityExecutorConfig);
    }

    @Test
    public void testCreateActivity() {
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        commandExecutor.executorCommand(req, createActivityExecutorConfig);
    }

    @Test
    public void testAddProduct(){
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executorCommand(req, addProductExecutorConfig);
    }

    /*
        验证商品主图同步 - AddProductPics
     */
    @Test
    public void testAddProductPics() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executorCommand(req, addProductPicsExecutorConfig);
    }

    @Test
    public void testDeleteProductPics() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executorCommand(req, deleteProductPicsExecutorConfig);
    }

    @Test
    public void testSuspendSale() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        long activityId = 334567;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        commandExecutor.executorCommand(req, suspendSaleExecutorConfig);
    }

    @Test
    public void testModifyBrandAndCategory(){
        String productId = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executorCommand(req, modifyBrandAndCategoryExecutorConfig);
    }

    /**
     * 同步活动商品数据
     *
     */
    @Test
    public void testSyncActivityProduct() {
        long productInActivityId = 286006;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(productInActivityId);

        commandExecutor.executorCommand(req, syncActivityProductExecutorConfig);
    }

    @Test
    public void testAutoRefreshProduct() {
        String productId = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executorCommand(req, autoOnShelfProductExecutorConfig);
    }

    public void testCatalogStockChange() {
        String productId = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executorCommand(req, catalogStockChangeExecutorConfig);
    }


    @Test
    public void testModifyActivity(){
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        commandExecutor.executorCommand(req, modifyActivityExecutorConfig);
    }

    /**
     *
     */
    @Test
    public void testSetOffTop(){
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        commandExecutor.executorCommand(req, setOffTopExecutorConfig);
    }

    /**
     *
     */
    @Test
    public void testDeleteProduct() throws MessageBusException {
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        commandExecutor.executorCommand(req, deleteProductExecutorConfig);
    }
}
