package com.ymatou.productsync.infrastructure.config.props;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

/**
 * 业务相关配置
 * Created by zhangyifan on 2016/12/21.
 */
@Component
@DisconfFile(fileName = "biz.properties")
public class BizProps {

    private int readCount;

    @DisconfFileItem(name = "readCount")
    public int getReadCount() {
        return readCount;
    }

    public void setReadCount(int readCount) {
        this.readCount = readCount;
    }
}
