package com.ymatou.productsync.domain.executor;

import com.google.common.collect.Maps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static Logger logger = LoggerFactory.getLogger(ExecutorConfigFactory.class);

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
        CmdTypeEnum cmdTypeEnum = null;
        try {
            cmdTypeEnum = CmdTypeEnum.valueOf(command.toUpperCase());
        } catch (Exception ex) {
            logger.error("Unknown sync command:{}", command, ex);
            return null;
        }
        if (!hashMap.containsKey(cmdTypeEnum)) {
            logger.error("Unknown sync command:{}", command);
            return null;
        }
        return hashMap.get(cmdTypeEnum);
    }
}
