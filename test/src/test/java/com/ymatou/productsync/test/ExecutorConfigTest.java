package com.ymatou.productsync.test;

import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.*;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.web.ProductSyncApplication;
import org.apache.http.util.Asserts;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

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
    private ModifyActivityExecutorConfig modifyActivityExecutorConfig;

    @Autowired
    private SetOffTopExecutorConfig setOffTopExecutorConfig;

    @Autowired
    private DeleteProductExecutorConfig deleteProductExecutorConfig;

    @Autowired
    private ProductPutoutExecutorConfig productPutoutExecutorConfig;

    @Autowired
    private ProductStockChangeExecutorConfig productStockChangeExecutorConfig;


    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private SyncActivityProductExecutorConfig syncActivityProductExecutorConfig;

    @Autowired
    private BatchSetOnShelfExecutorConfig batchSetOnShelfExecutorConfig;

    @Autowired
    private SetOnShelfUpdateStockNumExecutorConfig setOnShelfUpdateStockNumExecutorConfig;

    @Autowired
    private ModifyActivityPriceExecutorConfig modifyActivityPriceExecutorConfig;

    @Autowired
    private SetTopProductExecutorConfig setTopProductExecutorConfig;


    @Autowired
    private DeleteActivityExecutorConfig deleteActivityExecutorConfig;

    @Autowired
    private RemoveFromActivityExecutorConfig removeFromActivityExecutorConfig;

    @Test
    public void testSetOnTopExecutorConfig() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, setOnTopExecutorConfig);
    }

    @Test
    public void testAddActivity() {
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        commandExecutor.executeCommand(req, addActivityExecutorConfig);
    }

    @Test
    public void testConfirmActivity() {
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        commandExecutor.executeCommand(req, confirmActivityExecutorConfig);
    }

    @Test
    public void testCreateActivity() {
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        commandExecutor.executeCommand(req, createActivityExecutorConfig);
    }

    @Test
    public void testAddProduct() {
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, addProductExecutorConfig);
    }

    /*
        验证商品主图同步 - AddProductPics
     */
    @Test
    public void testAddProductPics() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, addProductPicsExecutorConfig);
    }

    @Test
    public void testDeleteProductPics() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, deleteProductPicsExecutorConfig);
    }

    @Test
    public void testSuspendSale() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        long activityId = 334567;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        commandExecutor.executeCommand(req, suspendSaleExecutorConfig);
    }

    @Test
    public void testModifyBrandAndCategory() {
        String productId = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, modifyBrandAndCategoryExecutorConfig);
    }

    /**
     * 同步活动商品数据
     */
    @Test
    public void testSyncActivityProduct() {
        long productInActivityId = 286006;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(productInActivityId);

        commandExecutor.executeCommand(req, syncActivityProductExecutorConfig);
    }

    @Test
    public void testAutoRefreshProduct() {
        String productId = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, autoOnShelfProductExecutorConfig);
    }

    public void testCatalogStockChange() {
        String productId = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, catalogStockChangeExecutorConfig);
    }


    @Test
    public void testModifyActivity() {
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        boolean isOk = commandExecutor.executeCommand(req, modifyActivityExecutorConfig);
        Asserts.check(isOk, "");

        //无效的直播Id
        long activityId2 = 0;
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setActivityId(activityId2);
        boolean isOk2 = commandExecutor.executeCommand(req2, modifyActivityExecutorConfig);
        Asserts.check(isOk2, "");

        //有直播商品的直播，要更新直播商品、商品信息
        long activityId3 = 149338;
        SyncByCommandReq req3 = new SyncByCommandReq();
        req3.setActivityId(activityId3);
        boolean isOk3 = commandExecutor.executeCommand(req3, modifyActivityExecutorConfig);
        Asserts.check(isOk3, "");
    }

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
        Asserts.check(checkOk, "");

        //测试置顶
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId);
        req2.setActivityId(activityId);
        boolean checkOk2 = commandExecutor.executeCommand(req2, setOnTopExecutorConfig);
        Asserts.check(checkOk2, "");
    }

    /**
     *
     */
    @Test
    public void testDeleteProduct() throws MessageBusException {
        //从直播中删商品
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        boolean checkOk = commandExecutor.executeCommand(req, deleteProductExecutorConfig);
        Asserts.check(checkOk, "");

        //pc删商品,不带直播id
        String productId2 = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId2);
        boolean checkRet = commandExecutor.executeCommand(req2, deleteProductExecutorConfig);
        Asserts.check(checkRet, "");
    }


    /**
     *
     */
    @Test
    public void testProductPutout() throws MessageBusException {
        //带直播id场景
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        //List<MongoData> update= productPutoutExecutorConfig.loadSourceData(activityId,productId);
        boolean check = commandExecutor.executeCommand(req, productPutoutExecutorConfig);
        Asserts.check(check, "");

        //不带直播id场景
        String productId2 = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId2);
        //List<MongoData> update2= productPutoutExecutorConfig.loadSourceData(0,productId);
        boolean checkOk = commandExecutor.executeCommand(req2, productPutoutExecutorConfig);
        Asserts.check(checkOk, "");
    }


    @Test
    public void testProductStockChange() throws MessageBusException {

        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check = commandExecutor.executeCommand(req, productStockChangeExecutorConfig);
        Asserts.check(check, "");

        //不存在的商品id，exception
        String productId2 = "7577884f-8606-4571-ba52-4881e89e111c";
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId2);
        //List<MongoData> update2 = productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check2 = commandExecutor.executeCommand(req2, productStockChangeExecutorConfig);
        Asserts.check(check2, "");


    }

    @Test
    public void testDeleteActivity() throws MessageBusException {

        long activityId = 25;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check = commandExecutor.executeCommand(req, deleteActivityExecutorConfig);
        Asserts.check(check, "");

    }

    @Test
    public void testRemoveFromActivity() throws MessageBusException {

        String productId = "f68f94f6-898a-4df7-823a-f187c0b62db3";
        long activityId = 3152;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check = commandExecutor.executeCommand(req, removeFromActivityExecutorConfig);
        Asserts.check(check, "");

        //测试一个不存在的直播id
        long activityId2 = 1;
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId);
        req2.setActivityId(activityId2);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check2 = commandExecutor.executeCommand(req2, removeFromActivityExecutorConfig);
        Asserts.check(check2, "");

    }

    @Test
    public void testBatchSetOnShelf() {
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        req.setProductId(productId);
        commandExecutor.executeCommand(req, batchSetOnShelfExecutorConfig);
    }

    @Test
    public void testSetOnShelfUpdateStockNum() {
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        req.setProductId(productId);
        boolean result = commandExecutor.executeCommand(req, setOnShelfUpdateStockNumExecutorConfig);
        Assert.assertTrue(result);
    }

    @Test
    public void testModifyActivityPrice() {
        String productId = "edc21ac6-5fc9-494c-9f36-110b841f75a0";
        long activityId = 18946;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        commandExecutor.executeCommand(req, modifyActivityPriceExecutorConfig);
    }

    @Test
    public void testSetTopProduct() {
        String productId = "f68f94f6-898a-4df7-823a-f187c0b62db3";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, setTopProductExecutorConfig);
    }
}
