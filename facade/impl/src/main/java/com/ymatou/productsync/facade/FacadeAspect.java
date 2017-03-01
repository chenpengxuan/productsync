/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.productsync.facade;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.ymatou.productsync.domain.executor.CommandExecutor;
import com.ymatou.productsync.domain.model.sql.SyncStatusEnum;

import com.ymatou.productsync.facade.model.req.SyncByCommandReq;
import com.ymatou.productsync.facade.model.resp.BaseResponse;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.LogWrapper;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.slf4j.MDC;
import org.springframework.beans.factory.annotation.Autowired;
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

    @Autowired
    private CommandExecutor commandExecutor;

    @Autowired
    private LogWrapper logWrapper;

    @Pointcut("execution(com.ymatou.productsync.facade.model.resp.BaseResponse com.ymatou.productsync.facade.*Facade.*(*)) && args(req)")
    public void executeFacade(SyncByCommandReq req) {
    }

    @Around("executeFacade(req)")
    public Object aroundFacadeExecution(ProceedingJoinPoint joinPoint, SyncByCommandReq req)
            throws InstantiationException, IllegalAccessException {

        Logger logger = DEFAULT_LOGGER;

        if (req == null) {
            logWrapper.recordErrorLog("{} Req: null", joinPoint.getSignature());
            return buildErrorResponse(joinPoint, SyncStatusEnum.IllegalArgEXCEPTION.getCode(), "request is null");
        }

        if (req.requireRequestId() && StringUtils.isEmpty(req.getRequestId())) {
            return buildErrorResponse(joinPoint, SyncStatusEnum.IllegalArgEXCEPTION.getCode(), "requestId not provided");
        }

        if (req.requireAppId() && StringUtils.isEmpty(req.getAppId())) {
            return buildErrorResponse(joinPoint, SyncStatusEnum.IllegalArgEXCEPTION.getCode(), "appId not provided");
        }

        long startTime = System.currentTimeMillis();

        if (StringUtils.isEmpty(req.getRequestId())) {
            req.setRequestId(Utils.uuid());
        }

        // log日志配有"logPrefix"占位符
        MDC.put(Constants.LOG_PREFIX, getRequestFlag(req));

        logger.info("Req:" + req.toString());

        Object resp = null;

        try {

            req.validate();
            resp = joinPoint.proceed(new Object[]{req});
        }
        catch (Throwable e) {
            logWrapper.recordErrorLog("Unknown error in executing request:{}", req, e);
            //前端可能将错误msg直接抛给用户
            resp = buildErrorResponse(joinPoint, SyncStatusEnum.FAILED.getCode(), "系统异常，请稍后重试");
            commandExecutor.updateTransactionInfo(req.getTransactionId(),SyncStatusEnum.FAILED);
        } finally {
            long consumedTime = System.currentTimeMillis() - startTime;
            logger.info("Resp:{}. Finished:{}. Consumed:{}ms.", resp, getRequestFlag(req), consumedTime);
            MDC.clear();
        }
        return resp;
    }

    private BaseResponse buildErrorResponse(ProceedingJoinPoint joinPoint, int errorCode, String errorMsg)
            throws InstantiationException, IllegalAccessException {

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        BaseResponse resp = (BaseResponse) ms.getReturnType().newInstance();
        resp.setErrorCode(errorCode);
        resp.setErrorMessage(errorMsg);
        return resp;

    }

    private String getRequestFlag(SyncByCommandReq req) {
        return req.getActionType() + "|" + req.getTransactionId();
    }
}
