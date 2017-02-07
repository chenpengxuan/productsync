package com.ymatou.productsync.test;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.executor.commandconfig.AddActivityExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.SetOnTopExecutorConfig;
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
@SpringBootTest(classes=ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class ExecutorConfigTest {
    @Autowired
    private SetOnTopExecutorConfig setOnTopExecutorConfig;

    @Autowired
    private AddActivityExecutorConfig addActivityExecutorConfig;

    @Autowired
    private CommandExecutor commandExecutor;

    @Test
    public void testSetOnTopExecutorConfig(){
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        List<MongoData> updateData= setOnTopExecutorConfig.loadSourceData(0,productId);
        commandExecutor.executorCommand(0,updateData);
    }

    @Test
    public  void testAddActivity()
    {
        long activityId=25;
        List<MongoData> updateData=addActivityExecutorConfig.loadSourceData(activityId,"");
        commandExecutor.executorCommand(0,updateData);
    }
}
