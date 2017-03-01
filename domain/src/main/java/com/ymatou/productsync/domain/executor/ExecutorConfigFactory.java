package com.ymatou.productsync.domain.executor;

import com.google.common.collect.Maps;
import com.ymatou.productsync.infrastructure.util.LogWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import javax.annotation.PostConstruct;
import java.util.HashMap;
import java.util.List;

/**
 * 业务指令器工厂
 * Created by chenpengxuan on 2017/1/19.
 */
@Component
public class ExecutorConfigFactory {

    @Autowired
    private LogWrapper logWrapper;

    /**
     * 业务场景指令与对应指令器映射关系的容器
     */
    private static HashMap<CmdTypeEnum, ExecutorConfig> hashMap = Maps.newHashMap();

    @Autowired
    private List<ExecutorConfig> executorConfigList;

    @PostConstruct
    public void init() {
        if (!CollectionUtils.isEmpty(executorConfigList)) {
            executorConfigList.forEach(config -> hashMap.put(config.getCommand(), config));
        }
    }

    public ExecutorConfig getCommand(String command) {
        CmdTypeEnum cmdTypeEnum;
        try {
            cmdTypeEnum = CmdTypeEnum.valueOf(command);
        } catch (Exception ex) {
            logWrapper.recordErrorLog("Unknown sync command:{}", command, ex);
            return null;
        }
        if (!hashMap.containsKey(cmdTypeEnum)) {
            logWrapper.recordErrorLog("Unknown sync command:{}", command);
            return null;
        }
        return hashMap.get(cmdTypeEnum);
    }
}
