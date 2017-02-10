/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.productsync.facade;

import com.alibaba.dubbo.common.utils.StringUtils;
import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.facade.model.BizException;
import com.ymatou.productsync.facade.model.ErrorCode;
import com.ymatou.productsync.facade.model.req.BaseRequest;
import com.ymatou.productsync.facade.model.resp.BaseResponse;
import com.ymatou.productsync.infrastructure.util.Utils;
import com.ymatou.productsync.infrastructure.constants.Constants;
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

    @Pointcut("execution(* com.ymatou.productsync.facade.*Facade.*(*)) && args(req)")
    public void executeFacade(BaseRequest req) {
    }

    @Around("executeFacade(req)")
    public Object aroundFacadeExecution(ProceedingJoinPoint joinPoint, BaseRequest req)
            throws InstantiationException, IllegalAccessException {

        Logger logger = DEFAULT_LOGGER;

        if (req == null) {
            logger.error("{} Req: null", joinPoint.getSignature());
            return buildErrorResponse(joinPoint, ErrorCode.ILLEGAL_ARGUMENT, "request is null");
        }

        if (req.requireRequestId() && StringUtils.isEmpty(req.getRequestId())) {
            return buildErrorResponse(joinPoint, ErrorCode.ILLEGAL_ARGUMENT, "requestId not provided");
        }

        if (req.requireAppId() && StringUtils.isEmpty(req.getAppId())) {
            return buildErrorResponse(joinPoint, ErrorCode.ILLEGAL_ARGUMENT, "appId not provided");
        }

        long startTime = System.currentTimeMillis();

        if (StringUtils.isEmpty(req.getRequestId())) {
            req.setRequestId(Utils.uuid());
        }

        // log日志配有"logPrefix"占位符
        MDC.put(Constants.LOG_PREFIX, getRequestFlag(req));

        logger.info("Req:" + req);

        Object resp = null;

        try {

            req.validate();
            resp = joinPoint.proceed(new Object[]{req});
        } catch (IllegalArgumentException e) {
            resp = buildErrorResponse(joinPoint, ErrorCode.ILLEGAL_ARGUMENT, e.getLocalizedMessage());
            logger.error("Invalid request: {}", req, e);
        } catch (BizException e) {
            //前端可能将错误msg直接抛给用户
            resp = buildErrorResponse(joinPoint, e.getErrorCode(), e.getLocalizedMessage());
            logger.error("Failed to execute request: {}, Error:{}", req.getRequestId(),
                    e.getErrorCode() + "|" + e.getErrorCode().getMessage(), e);
        }catch (MessageBusException e) {
            //前端可能将错误msg直接抛给用户
            resp = buildErrorResponse(joinPoint, ErrorCode.UNKNOWN, "系统异常，请稍后重试");
            logger.error("Unknown error in executing request:{}", req, e);
        }
        catch (Throwable e) {
            //前端可能将错误msg直接抛给用户
            resp = buildErrorResponse(joinPoint, ErrorCode.UNKNOWN, "系统异常，请稍后重试");
            logger.error("Unknown error in executing request:{}", req, e);
        } finally {
            long consumedTime = System.currentTimeMillis() - startTime;
            logger.info("Resp:{}. Finished:{}. Consumed:{}ms.", resp, getRequestFlag(req), consumedTime);
            MDC.clear();
        }
        return resp;
    }

    private BaseResponse buildErrorResponse(ProceedingJoinPoint joinPoint, ErrorCode errorCode, String errorMsg)
            throws InstantiationException, IllegalAccessException {

        MethodSignature ms = (MethodSignature) joinPoint.getSignature();
        BaseResponse resp = (BaseResponse) ms.getReturnType().newInstance();
        resp.setErrorCode(errorCode);
        resp.setErrorMessage(errorMsg);
        return resp;

    }

    private String getRequestFlag(BaseRequest req) {
        return req.getClass().getSimpleName() + "|" + req.getRequestId();
    }
}
