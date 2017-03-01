/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */
package com.ymatou.productsync.infrastructure.util;

import com.ymatou.productsync.infrastructure.config.props.BizProps;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

/**
 * 异常信息包装器
 * @author chenpengxuan
 */
@Component
public class LogWrapper {

    private static final Logger logger = LoggerFactory.getLogger(LogWrapper.class);

    @Autowired
    private BizProps bizProps;

    /**
     * 异常记录
     * @param msg
     */
    public void recordErrorLog(String msg){
        if(bizProps.isBizExceptionWarning()){
            logger.error(msg);
        }else{
            logger.debug(msg);
        }
    }

    /**
     * 异常记录
     * @param msg
     * @param objects
     */
    public void recordErrorLog(String msg,Object... objects){
        if(bizProps.isBizExceptionWarning()){
            logger.error(msg,objects);
        }else{
            logger.debug(msg,objects);
        }
    }
}
