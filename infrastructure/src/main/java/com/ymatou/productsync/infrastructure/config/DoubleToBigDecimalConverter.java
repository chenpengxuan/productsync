package com.ymatou.productsync.infrastructure.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by zhangyifan on 2016/12/21.
 */
@Component
public class DoubleToBigDecimalConverter implements Converter<Long, BigDecimal> {

    private static BigDecimal CONST100 = new BigDecimal(100);

    @Override
    public BigDecimal convert(Long source) {
        return new BigDecimal(source).divide(CONST100).setScale(2);
    }
}