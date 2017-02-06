package com.ymatou.productsync.test;

import com.ymatou.productsync.domain.executor.commandconfig.SetOnTopExecutorConfig;
import com.ymatou.productsync.domain.executor.commandconfig.TestIOC;
import com.ymatou.productsync.domain.model.UpdateData;
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
@SpringBootTest(classes=ProductSyncApplication.class)// 指定我们SpringBoot工程的Application启动类
public class ExecutorConfigTest {
    @Autowired
    private SetOnTopExecutorConfig setOnTopExecutorConfig;

    @Test
    public void testSetOnTopExecutorConfig(){
        String productId = "992b3749-4379-4260-b05b-24e734423f9f";
        UpdateData updateData= setOnTopExecutorConfig.loadSourceData(0,productId);
    }

    @Autowired
    private TestIOC testIOC;

    @Test
    public void testIoc(){
        String aaa = "";

    }
}