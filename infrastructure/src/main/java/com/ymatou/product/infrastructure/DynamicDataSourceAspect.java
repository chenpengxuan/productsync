package com.ymatou.product.infrastructure;

import org.apache.commons.lang.time.StopWatch;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * Created by chenpengxuan on 2016/9/1.
 */
@Aspect
@Order(-1) // 保证该AOP在@Transactional之前执行
@Component
public class DynamicDataSourceAspect {

    private final Logger logger = LoggerFactory.getLogger(DynamicDataSourceAspect.class);
    private final StopWatch sw = new StopWatch();
    @Before("execution(* com.ymatou.product.repository.*.*(..))")
    public void before(JoinPoint point) {
        Object target = point.getTarget();
        String method = point.getSignature().getName();

        Class<?>[] classz = target.getClass().getInterfaces();

        Class<?>[] parameterTypes = ((MethodSignature) point.getSignature()).getMethod().getParameterTypes();
        try {
            Method m = classz[0].getMethod(method, parameterTypes);
            if (m != null && m.isAnnotationPresent(TargetDataSource.class)) {
                TargetDataSource data = m.getAnnotation(TargetDataSource.class);
                DynamicDataSourceContextHolder.setDataSourceType(data.value());
                logger.info("DataSource：" + data.value());
                sw.reset();
                sw.start();
            }
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }

    @After("execution(* com.ymatou.product.repository.*.*(..))")
    public void restoreDataSource(JoinPoint point) {
        DynamicDataSourceContextHolder.clearDataSourceType();
        String method = point.getSignature().getName();
        try{
            sw.stop();
            logger.debug(String.format("Repository_%s_耗时_%d：",method,sw.getTime()));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }
    }
}