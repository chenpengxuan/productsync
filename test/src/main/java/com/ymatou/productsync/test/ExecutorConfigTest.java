package com.ymatou.productsync.test;

import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.*;
import com.ymatou.productsync.domain.model.MongoData;
import com.ymatou.productsync.web.ProductSyncApplication;
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
    private  ModifyActivityExecutorConfig modifyActivityExecutorConfig ;

    @Autowired
    private  SetOffTopExecutorConfig setOffTopExecutorConfig ;

    @Autowired
    private  DeleteProductExecutorConfig deleteProductExecutorConfig ;

    @Autowired
    private  ProductPutoutExecutorConfig productPutoutExecutorConfig ;

    @Autowired
    private  ProductStockChangeExecutorConfig productStockChangeExecutorConfig ;

    @Autowired
    private CommandExecutor commandExecutor;

    @Test
    public void testSetOnTopExecutorConfig() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        List<MongoData> updateData = setOnTopExecutorConfig.loadSourceData(0, productId);
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testAddActivity() {
        long activityId = 157242;
        List<MongoData> updateData = addActivityExecutorConfig.loadSourceData(activityId, "");
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testConfirmActivity() {
        long activityId = 157242;
        List<MongoData> updateData = confirmActivityExecutorConfig.loadSourceData(activityId, "");
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testCreateActivity() {
        long activityId = 157242;
        List<MongoData> updateData = createActivityExecutorConfig.loadSourceData(activityId, "");
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testAddProduct(){
        String productId = "f68f94f6-898a-4df7-823a-f187c0b62db3";
        List<MongoData> updateData = addProductExecutorConfig.loadSourceData(3152, productId);
        commandExecutor.executorCommand(25, updateData);
    }

    /*
        验证商品主图同步 - AddProductPics
     */
    @Test
    public void testAddProductPics() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        List<MongoData> updateData = addProductPicsExecutorConfig.loadSourceData(0, productId);
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testDeleteProductPics() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        List<MongoData> updateData = deleteProductPicsExecutorConfig.loadSourceData(0, productId);
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testSuspendSale() {
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        long activityId = 334567;
        List<MongoData> updateData = suspendSaleExecutorConfig.loadSourceData(activityId, productId);
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testModifyBrandAndCategory(){
        String productId = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        List<MongoData> updateData = modifyBrandAndCategoryExecutorConfig.loadSourceData(0, productId);
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testAutoRefreshProduct() {
        String productId = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        List<MongoData> updateData = autoOnShelfProductExecutorConfig.loadSourceData(0, productId);
        commandExecutor.executorCommand(0, updateData);
    }

    @Test
    public void testCatalogStockChange() {
        String productid = "acf23898-c735-4f70-adc2-f8e09e60d19f";
        List<MongoData> updateData = catalogStockChangeExecutorConfig.loadSourceData(0, productid);
        commandExecutor.executorCommand(0, updateData);
    }

//
    @Test
    public void testModifyActivity(){
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";

        List<MongoData> update= modifyActivityExecutorConfig.loadSourceData(activityId,"");
        commandExecutor.executorCommand(0, update);
    }

    /**
     *
     */
    @Test
    public void testSetOffTop(){
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";

        List<MongoData> update= setOffTopExecutorConfig.loadSourceData(activityId,productId);
        commandExecutor.executorCommand(0, update);
    }

    /**
     *
     */
    @Test
    public void testDeleteProduct() throws MessageBusException {
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";

        List<MongoData> update= deleteProductExecutorConfig.loadSourceData(activityId,productId);
        commandExecutor.executorCommand(0, update);
    }


    /**
     * 有错误 Error
     */
    @Test
    public void testProductPutout() throws MessageBusException {
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";

        List<MongoData> update= productPutoutExecutorConfig.loadSourceData(activityId,productId);
        commandExecutor.executorCommand(0, update);
    }


    @Test
    public void testProductStockChange() throws MessageBusException {
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";

        List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(activityId,productId);
        commandExecutor.executorCommand(0, update);
    }

}
