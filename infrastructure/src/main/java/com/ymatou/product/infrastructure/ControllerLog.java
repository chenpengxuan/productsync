package com.ymatou.product.infrastructure;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.AfterReturning;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import java.util.Arrays;

/**
 * Created by chenpengxuan on 2016/9/6.
 */
@Aspect
@Component
public class ControllerLog {
    private final Logger logger = LoggerFactory.getLogger(ControllerLog.class);

    @Before("execution( @org.springframework.web.bind.annotation.RequestMapping * *(..))")
    public void SetRequestLog(JoinPoint pc){
         logger.debug("Request："+ pc.getSignature().getDeclaringTypeName() +
                 "." + pc.getSignature().getName() + Arrays.toString(pc.getArgs()));
    }

    @AfterReturning(pointcut="execution( @org.springframework.web.bind.annotation.RequestMapping * *(..))",returning = "returnValue")
    public void SetResponseLog(JoinPoint pc,Object returnValue){
        logger.debug("Response：" + returnValue.toString());
    }
}
