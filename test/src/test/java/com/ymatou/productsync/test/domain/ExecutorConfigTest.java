package com.ymatou.productsync.test.domain;

import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.model.mongo.MongoOperationTypeEnum;
import com.ymatou.productsync.domain.model.mongo.MongoQueryData;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;
import com.ymatou.productsync.domain.mongorepo.MongoRepository;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.web.ProductSyncApplication;
import org.apache.http.util.Asserts;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.Date;
import java.util.HashMap;
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
    private CommandExecutor commandExecutor;

    @Autowired
    private MongoRepository mongoRepository;

    @Test
    public void testUpdateTransactionInfo() {
        int transationId = 10;
        commandExecutor.updateTransactionInfo(transationId, SyncStatusEnum.BizEXCEPTION);
    }
    @Test
    public void testQueryMongoByDate(){
        MongoQueryData mongoQueryData = new MongoQueryData();
        mongoQueryData.setDistinctKey("");
        Map<String,Object> tempMap = new HashMap<>();
        Map<String,Object> testMap = new HashMap<>();
        tempMap.put("$gt", new Date());
        testMap.put("end",tempMap);
//        testMap.put("spid","3be45de7-1301-42f7-888c-278657e98336");
        mongoQueryData.setMatchCondition(testMap);
        mongoQueryData.setTableName(Constants.LiveProudctDb);
        mongoQueryData.setOperationType(MongoOperationTypeEnum.SELECTMANY);
        List<Map<String,Object>> mapList = mongoRepository.queryMongo(mongoQueryData);
        Asserts.check(mapList != null && !mapList.isEmpty(),null);
    }
}
