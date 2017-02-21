package com.ymatou.productsync.test.domain;

import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.ExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.*;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.sqlrepo.TestCommandQuery;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.web.ProductSyncApplication;
import org.apache.http.util.Asserts;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * 场景业务指令器test
 * Created by chenpengxuan on 2017/1/23.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class ExecutorConfigTest {
    @Autowired
    private TestCommandQuery commandQuery;

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
    private ModifyPutawayProductInfoExecutorConfig modifyPutawayProductInfoExecutorConfig;

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
        //#1正常添加直播
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        boolean issuccess = commandExecutor.executeCommand(req, addActivityExecutorConfig);
        Asserts.check(issuccess, "测试添加直播fail!");
    }

    @Test
    public void testAddActivityException() {
        //#2sql没有的直播
        SyncByCommandReq req = new SyncByCommandReq();
        long nActivityId = 1572420;
        req.setActivityId(nActivityId);
        try {
            commandExecutor.executeCommand(req, addActivityExecutorConfig);
        } catch (BizException ex) {
            Asserts.check(ex.getMessage() == "getActivityInfo为空", "操作不存在的直播添加测试fail！");
        }
    }

    @Test
    public void testConfirmActivity() {
        //#1正常确认直播
        long activityId = 157242;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        boolean issucess = commandExecutor.executeCommand(req, confirmActivityExecutorConfig);
        Asserts.check(issucess, "测试确认直播车fail！");
    }

    @Test
    public void testConfirmActivityException() {
        SyncByCommandReq req = new SyncByCommandReq();
        //#2sql没有的直播
        long nActivityId = 1572420;
        req.setActivityId(nActivityId);
        try {
            commandExecutor.executeCommand(req, confirmActivityExecutorConfig);
        } catch (BizException ex) {
            Asserts.check(ex.getMessage() == "getActivityInfo为空", "测试没有的直播确认fail！");
        }
    }

    @Test
    public void testCreateActivity() {
        SyncByCommandReq req = new SyncByCommandReq();
        //#1正常创建直播
        long activityId = 157242;

        req.setActivityId(activityId);
        boolean issuccess = commandExecutor.executeCommand(req, createActivityExecutorConfig);
        Asserts.check(issuccess, "正常创建直播fail！");
    }

    @Test
    public void testCreateActivityException() {
        //#2sql没有的直播
        SyncByCommandReq req = new SyncByCommandReq();
        long nActivityId = 1572420;
        req.setActivityId(nActivityId);
        try {
            commandExecutor.executeCommand(req, confirmActivityExecutorConfig);
        } catch (BizException ex) {
            Asserts.check(true, "测试没有的直播确认fail！");
        }
    }

    @Test
    public void testAddProduct() {
        String productId = "67a3e031-5b99-408f-b40f-3f76d3aa9a81";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, addProductExecutorConfig);
    }

    @Test
    public void testUpdateProduct() {
        String productId = "e884549d-453c-416c-ab9d-6a6990ac7202";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        commandExecutor.executeCommand(req, modifyPutawayProductInfoExecutorConfig);
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

    @Test
    public void testModifyBrandAndCategoryException() {
        SyncByCommandReq req = new SyncByCommandReq();
//        #3不存在商品
        req.setProductId("7577884f-8606-4571-ba52-4881e89e660cc");
        try {
            commandExecutor.executeCommand(req, modifyBrandAndCategoryExecutorConfig);
        } catch (
                BizException ex) {
            Asserts.check(true, "");
        }
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
//        #1商品自动上架
        List<Map<String, Object>> tproducts = commandQuery.getProduct();
        Map<String, Object> prod = tproducts.stream().findFirst().orElse(Collections.emptyMap());
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(prod.get("sProductId").toString());
        boolean success = commandExecutor.executeCommand(req, autoOnShelfProductExecutorConfig);
        Asserts.check(success, "测试商品自动上架fail！");
    }

    @Test
    public void testCatalogStockChange() {
        SyncByCommandReq req = new SyncByCommandReq();
//        #1 正常商品规格库存测试
        List<Map<String, Object>> tproducts = commandQuery.getProduct();
        Map<String, Object> prod = tproducts.stream().findFirst().orElse(Collections.emptyMap());
        req.setProductId(prod.get("sProductId").toString());
        boolean success = commandExecutor.executeCommand(req, catalogStockChangeExecutorConfig);
        Asserts.check(success, "正常商品规格库存测试fail!");
    }

    @Test
    public void testCatalogStockChangeException() {
        SyncByCommandReq req = new SyncByCommandReq();
//        #2 不存在的商品规格库存测试
        req.setProductId("7577884f-8606-4571-ba52-4881e89e660cc");
        try {
            boolean success1 = commandExecutor.executeCommand(req, catalogStockChangeExecutorConfig);
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }

    @Test
    public void testUpdateTransactionInfo() {
        int transationId = 10;
        commandExecutor.updateTransactionInfo(transationId, SyncStatusEnum.BizEXCEPTION);
    }

    @Test
    public void testModifyActivity() {
        long activityId = 157305;
        //String productId = "e06c1578-e88e-43c1-a763-519e9fb60701";
        SyncByCommandReq req = new SyncByCommandReq();
        //req.setProductId(productId);
        req.setActivityId(activityId);
        boolean isOk = commandExecutor.executeCommand(req, modifyActivityExecutorConfig);
        Asserts.check(isOk, "");

        //无效的直播Id
//        long activityId2 = 0;
//        SyncByCommandReq req2 = new SyncByCommandReq();
//        req2.setActivityId(activityId2);
//        boolean isOk2 = commandExecutor.executeCommand(req2, modifyActivityExecutorConfig);
//        Asserts.check(!isOk2, "");

        //有直播商品的直播，要更新直播商品、商品信息
        long activityId3 = 157305;
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
        Asserts.check(checkOk, "测试取消置顶fail!");
    }

    public void testSetOffTopException() {
        //测试置顶
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req2 = new SyncByCommandReq();
        req2.setProductId(productId);
        req2.setActivityId(activityId);
        boolean checkOk2 = commandExecutor.executeCommand(req2, setOnTopExecutorConfig);
        Asserts.check(checkOk2, "测试置顶fail");
    }

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

    @Test
    public void testDeleteProductException() {
        //pc删商品,不带直播id
        try {
            String productId2 = "7577884f-8606-4571-ba52-4881e89e660c";
            SyncByCommandReq req2 = new SyncByCommandReq();
            req2.setProductId(productId2);
            boolean checkRet = commandExecutor.executeCommand(req2, deleteProductExecutorConfig);
            Asserts.check(checkRet, "从直播中删除商品fail！");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }


    /**
     *
     */
    @Test
    public void testProductPutout() {
        //带直播id场景
        long activityId = 3152;
        String productId = "f68f94f6-898a-4df7-823a-f187c0b62db3";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        //List<MongoData> update= productPutoutExecutorConfig.loadSourceData(activityId,productId);
        boolean check = commandExecutor.executeCommand(req, productPutoutExecutorConfig);
        Asserts.check(check, "");
    }

    @Test
    public void testProductPutoutException() {
        //不带直播id场景
        try {
            String productId2 = "7577884f-8606-4571-ba52-4881e89e660c";
            SyncByCommandReq req2 = new SyncByCommandReq();
            req2.setProductId(productId2);
            //List<MongoData> update2= productPutoutExecutorConfig.loadSourceData(0,productId);
            boolean checkOk = commandExecutor.executeCommand(req2, productPutoutExecutorConfig);
            Asserts.check(checkOk, "");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }


    @Test
    public void testProductStockChange() {
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check = commandExecutor.executeCommand(req, productStockChangeExecutorConfig);
        Asserts.check(check, "测试商品库存增减,价格修改fail!");
    }

    @Test
    public void TestProductStockChangeException() {
        //不存在的商品id，exception
        try {
            String productId2 = "7577884f-8606-4571-ba52-4881e89e111c";
            SyncByCommandReq req2 = new SyncByCommandReq();
            req2.setProductId(productId2);
            //List<MongoData> update2 = productStockChangeExecutorConfig.loadSourceData(0,productId);
            boolean check2 = commandExecutor.executeCommand(req2, productStockChangeExecutorConfig);
            Asserts.check(!check2, "测试不存在的商品id商品库存增减,价格修改fail!");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }

    @Test
    public void testDeleteActivity() {
        long activityId = 25;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check = commandExecutor.executeCommand(req, deleteActivityExecutorConfig);
        Asserts.check(check, "测试删除直播fail！");
    }

    @Test
    public void testRemoveFromActivity() {
        String productId = "5fbcbc07-16fc-4186-9729-90ba7ba53e57";
        long activityId = 149338;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        req.setActivityId(activityId);
        //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
        boolean check = commandExecutor.executeCommand(req, removeFromActivityExecutorConfig);
        Asserts.check(check, "测试商品移除直播fail！");
    }

    @Test
    public void testRemoveFromActivityException() {
        try {
            //测试一个不存在的直播id
            long activityId2 = 1;
            String productId = "5fbcbc07-16fc-4186-9729-90ba7ba53e57";
            SyncByCommandReq req2 = new SyncByCommandReq();
            req2.setProductId(productId);
            req2.setActivityId(activityId2);
            //List<MongoData> update= productStockChangeExecutorConfig.loadSourceData(0,productId);
            boolean check2 = commandExecutor.executeCommand(req2, removeFromActivityExecutorConfig);
            Asserts.check(check2, "测试一个不存在的直播,商品移除直播fail");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }

    @Test
    public void testBatchSetOnShelf() {
        long activityId = 157242;
        String productId = "7577884f-8606-4571-ba52-4881e89e660c";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        req.setProductId(productId);
        boolean success = commandExecutor.executeCommand(req, batchSetOnShelfExecutorConfig);
        Asserts.check(success, "测试批量C商品上架fail!");
    }

    @Test
    public void testSetOnShelfUpdateStockNum() {
//        #1正常批量上架商品【商品售罄】
//        List<Map<String, Object>> query = commandQuery.getLiveProduct();
//        Map<String, Object> prod = query.stream().findFirst().orElse(Collections.emptyMap());
//        SyncByCommandReq req = new SyncByCommandReq();
//        req.setProductId(prod.get("sProductId").toString());
//        req.setActivityId(Integer.parseInt(prod.get("iActivityId").toString()));
//        boolean success1 = commandExecutor.executeCommand(req, setOnShelfUpdateStockNumExecutorConfig);
//        Asserts.check(success1, "测试正常批量上架商品【商品售罄】fail！");
        long activityId = 157589;
        String productId = "9022e96f-f273-47d0-a2a4-de2085235b43";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(activityId);
        req.setProductId(productId);
        boolean success1 = commandExecutor.executeCommand(req, setOnShelfUpdateStockNumExecutorConfig);
        Asserts.check(success1, "测试正常批量上架商品【商品售罄】fail！");
    }

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

    @Test
    public void testModifyActivityPriceNpException() {
//        #2测试活动商品没有的情况
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(0);
        try {
            boolean success2 = commandExecutor.executeCommand(req, modifyActivityPriceExecutorConfig);
            Asserts.check(success2, "测试活动商品没有的情况fail！");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }

    @Test
    public void testModifyActivityPriceNcException() {
//        #3测试活动商品规格没有的情况
        SyncByCommandReq req = new SyncByCommandReq();
        List<Map<String, Object>> query3 = commandQuery.getInvalidActivityProduct();
        Map<String, Object> prod1 = query3.stream().findFirst().orElse(Collections.emptyMap());
        req.setActivityId(Integer.parseInt(prod1.get("iProductInActivityId").toString()));
        try {
            boolean success3 = commandExecutor.executeCommand(req, modifyActivityPriceExecutorConfig);
            Asserts.check(success3, "测试活动商品规格没有的情况fail!");
        } catch (BizException ex) {
            Asserts.check(true, "");
        }
    }

    @Test
    public void testSetTopProduct() {
        SyncByCommandReq req = new SyncByCommandReq();
        //#1正常设置橱窗推荐商品
        try {
            List<Map<String, Object>> tproducts = commandQuery.getTopProduct();
            Map<String, Object> prod = tproducts.stream().findFirst().orElse(Collections.emptyMap());
            req.setProductId(prod.get("sProductId").toString());
            boolean success1 = commandExecutor.executeCommand(req, setTopProductExecutorConfig);
            Asserts.check(success1, "正常设置橱窗推荐商品fail！");
        } catch (Exception ex) {
            Asserts.check(false, "正常设置橱窗推荐商品fail！" + ex);
        }

        //#2正常取消设置橱窗推荐产品
        try {
            List<Map<String, Object>> ntprods = commandQuery.getNotTopProduct();
            Map<String, Object> nprod = ntprods.stream().findFirst().orElse(Collections.emptyMap());
            req.setProductId(nprod.get("sProductId").toString());
            boolean success2 = commandExecutor.executeCommand(req, setTopProductExecutorConfig);
            Asserts.check(success2, "正常取消设置橱窗推荐产品fail！");
        } catch (Exception ex) {
            Asserts.check(false, "正常取消设置橱窗推荐产品fail！" + ex);
        }

        //#3操作不存在的商品
        req.setProductId("edc21ac6-5fc9-494c-9f36-110b841f75a00");
        try {
            boolean success3 = commandExecutor.executeCommand(req, setTopProductExecutorConfig);
            Asserts.check(success3, "操作不存在的商品设置橱窗推荐产品fail！");
        } catch (Exception ex) {
            Asserts.check(false, "操作不存在的商品设置橱窗推荐产品fail！" + ex);
        }

    }
}
