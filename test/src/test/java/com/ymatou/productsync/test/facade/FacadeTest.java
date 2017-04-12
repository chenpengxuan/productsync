package com.ymatou.productsync.test.facade;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.AddProductExecutorConfig;
import com.ymatou.productsync.domain.model.mongo.MongoData;
import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.web.ProductSyncApplication;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * facade test
 * Created by chenpengxuan on 2017/4/6.
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringBootTest(classes = ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class FacadeTest {
    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private AddProductExecutorConfig addProductExecutorConfig;

    @Test
    public void syncProductChangeRangeTest(){
        String productId = "dd5e3204-af80-4cfb-a236-3e1aebef587e";
        SyncByCommandReq req = new SyncByCommandReq();
        req.setProductId(productId);
        List<MongoData> mongoDataList = addProductExecutorConfig
                .loadSourceData(req.getActivityId(),req.getProductId());
        commandExecutor.syncProductChangeRange(req,mongoDataList);
    }
}
