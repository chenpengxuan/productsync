package com.ymatou.productsync.infrastructure.config.props;

/**
 * Created by zhangyifan on 2016/12/09.
 */

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

@Component
@DisconfFile(fileName = "mongo.properties")
public class MongoProps {
    private String mongoProductUrl;
    private String mongoProductName;

    @DisconfFileItem(name = "mongoProductUrl")
    public String getMongoProductUrl() {
        return mongoProductUrl;
    }

    public void setMongoProductUrl(String mongoProductUrl) {
        this.mongoProductUrl = mongoProductUrl;
    }

    @DisconfFileItem(name = "mongoProductName")
    public String getMongoProductName() {
        return mongoProductName;
    }

    public void setMongoProductName(String mongoProductName) {
        this.mongoProductName = mongoProductName;
    }
}
