package com.ymatou.product.domain;

import com.baidu.disconf.client.common.annotations.DisconfFile;
import com.baidu.disconf.client.common.annotations.DisconfFileItem;
import org.springframework.stereotype.Component;

/**
 * 业务配置类
 * Created by chenpengxuan on 2016/12/26.
 */
@Component
@DisconfFile(fileName = "biz.properties")
public class BizConfig {
    //业务凭据初始化时间差（单位：分钟）
    private int compensatoinMinute;
    //同步数据上限
    private int getFailureCountLimit;

    @DisconfFileItem(name = "CompensatoinMinute")
    public int getCompensatoinMinute() {
        return compensatoinMinute;
    }

    public void setCompensatoinMinute(int compensatoinMinute) {
        this.compensatoinMinute = compensatoinMinute;
    }
    @DisconfFileItem(name = "GetFailureCountLimit")
    public int getGetFailureCountLimit() {
        return getFailureCountLimit;
    }

    public void setGetFailureCountLimit(int getFailureCountLimit) {
        this.getFailureCountLimit = getFailureCountLimit;
    }
}
