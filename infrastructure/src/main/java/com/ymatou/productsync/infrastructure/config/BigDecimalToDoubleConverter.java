package com.ymatou.productsync.infrastructure.config;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

/**
 * Created by zhangyifan on 2016/12/21.
 */
@Component
public class BigDecimalToDoubleConverter implements Converter<BigDecimal, Long> {

    private static BigDecimal CONST100 = new BigDecimal(100);

    @Override
    public Long convert(BigDecimal source) {
        return source.multiply(CONST100).longValue();
    }
}