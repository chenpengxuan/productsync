package com.ymatou.productsync.test.facade;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.SyncActivityProductExecutorConfig;
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
    private SyncActivityProductExecutorConfig syncActivityProductExecutorConfig;

    @Test
    public void syncProductChangeRangeTest(){
        long productInActivityId = 286177;
        SyncByCommandReq req = new SyncByCommandReq();
        req.setActivityId(productInActivityId);
        List<MongoData> mongoDataList = syncActivityProductExecutorConfig
                .loadSourceData(req.getActivityId(),req.getProductId());
        commandExecutor.syncProductChangeRange(req,mongoDataList);
    }
}
