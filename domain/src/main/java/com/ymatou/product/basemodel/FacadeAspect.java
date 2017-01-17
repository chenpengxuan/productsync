/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.product.basemodel;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.stereotype.Component;

/**
 * Facade实现方法的AOP.
 * <p>
 * 实现与业务无关的通用操作。
 * <p>
 * 1，日志
 * <p>
 * 2，异常处理等
 *
 * @author tuwenjie
 */
@Aspect
@Component
public class FacadeAspect {

    private static final Logger DEFAULT_LOGGER = LoggerFactory.getLogger(FacadeAspect.class);

    @Pointcut("execution(* com.ymatou.tradingquery.facade.*Facade.*(*)) && args(req)")
    public void executeFacade(BaseRequest req) {
    }

    @Around("executeFacade(req)")
    public Object aroundFacadeExecution(ProceedingJoinPoint joinPoint, BaseRequest req)
            throws InstantiationException, IllegalAccessException {

        Logger logger = DEFAULT_LOGGER;

        if (req == null) {
            logger.error("{} Request请求为空: null", joinPoint.getSignature());
            return buildErrorResponse(joinPoint, ErrorCode.ILLEGAL_ARGUMENT, "request is null");
        }
        long startTime = System.currentTimeMillis();

        // log日志配有"logPrefix"占位符
        MDC.put(Constants.LOG_PREFIX, getRequestFlag(req));

        logger.debug("Recv:" + req);

        Object resp = null;

        try {
            req.Validate();
            resp = joinPoint.proceed(new Object[]{req});

        } catch (IllegalArgumentException e) {
            resp = buildErrorResponse(joinPoint, ErrorCode.ILLEGAL_ARGUMENT, e.getLocalizedMessage());
            logger.error("无效参数: {}", req, e);
        } catch (ApiException e) {
            //前端可能将错误msg直接抛给用户
            resp = buildErrorResponse(joinPoint, e.getErrorCode(), e.getLocalizedMessage());
            logger.warn("请求执行失败: {}, Error:{}", req.getTransactionId(),
                    e.getErrorCode() + "|" + e.getErrorCode().getMessage(), e);
        } catch (Throwable e) {
            //前端可能将错误msg直接抛给用户
            resp = buildErrorResponse(joinPoint, ErrorCode.UNKNOWN, "系统异常，请稍后重试");
            logger.error("Unknown error in executing request:{}", req, e);
        } finally {
            logger.debug("Resp:" + resp);
            MDC.clear();
        }

        long consumedTime = System.currentTimeMillis() - startTime;

        logger.info("Finished {}, Consumed:{}ms", getRequestFlag(req), consumedTime);

        generateSlowLog(consumedTime, logger, req);

        return resp;
    }

    private BaseResponse buildErrorResponse(ProceedingJoinPoint joinPoint, ErrorCode errorCode, String errorMsg)
            throws InstantiationException, IllegalAccessException {

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        BaseResponse resp = (BaseResponse) ms.getReturnType().newInstance();
        resp.setErrorCode(errorCode);
        resp.setErrorMessage(errorMsg);
        resp.setSuccess(false);
        return resp;

    }

    private String getRequestFlag(BaseRequest req) {
        return req.getClass().getSimpleName() + "|" + req.getTransactionId();
    }

    private void generateSlowLog(long consumedTime, Logger logger, BaseRequest req) {
        if (consumedTime >= 500L) {
            String title = "gt500ms";
            if (consumedTime > 1000L) {
                title += "1s";
            }
            if (consumedTime > 2000L) {
                title += "2s";
            }
            if (consumedTime > 5000L) {
                title += "5s";
            }
            if (consumedTime > 10000L) {
                title += "10s";
            }
            logger.debug("slow_{}:{}ms; req:{}", title, consumedTime, req);
        }
    }
}
