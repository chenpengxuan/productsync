package com.ymatou.product.controller;

import com.ymatou.product.basemodel.BaseResponse;
import com.ymatou.product.basemodel.BusinessCode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;

/**
 * Created by chenpengxuan on 2016/9/5.
 */
@ControllerAdvice
public class ExceptionFilter {

    private static Logger logger = LoggerFactory.getLogger(ExceptionFilter.class);

    @ExceptionHandler(value = {Exception.class})
    public  BaseResponse<BaseResponse> handleOtherExceptions(final Exception ex, final WebRequest req) {
        logger.error(ex.getMessage(),ex);
        BaseResponse<BaseResponse> response = new BaseResponse<BaseResponse>();
        response.setErrorMessage(ex.getCause().getMessage());
        response.setSuccess(false);
        response.setBusinessCode(BusinessCode.UNKNOWN);
        return response;
    }
}
