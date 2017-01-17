package com.ymatou.product.infrastructure;/*
 * (C) Copyright 2016 Ymatou (http://www.ymatou.com/). All rights reserved.
 */

import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.base.Optional;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateUtils;
import org.bson.types.ObjectId;
import org.joda.time.DateTime;
import org.joda.time.Days;

import java.math.BigDecimal;
import java.math.MathContext;
import java.math.RoundingMode;
import java.util.Arrays;
import java.util.Date;

/**
 * @author tuwenjie 2016年5月7日 下午4:17:39
 *
 */
public class Utils {

    public static String uuid() {
        return new ObjectId().toHexString();
    }

    public static String trimAndLeft(String src, int n) {
        return StringUtils.left(StringUtils.trim(src), n);
    }

    public static String toDotNetCompatibleJson(Object o) {
        JacksonConfig config = new JacksonConfig();
        ObjectMapper mapper = config.getContext(null);
        try {
            return mapper.writeValueAsString(o);
        } catch (Exception e) {
            throw new RuntimeException("Failed to convert object to json:" + o, e);
        }
    }


    public static <T> T parseDotNetCompatibleJson(String json, Class<T> valueType) {
        JacksonConfig config = new JacksonConfig();
        ObjectMapper mapper = config.getContext(null);
        try {
            return mapper.readValue(json, valueType);
        } catch (Exception e) {
            throw new RuntimeException("Failed to parse json to class. json:" + json + " class:" + valueType, e);
        }
    }


    /**
     * 根据权重平摊一个数额
     * 
     * @param total
     * @param weights
     * @return
     */
    public static BigDecimal[] apportionByWeights(BigDecimal total, BigDecimal[] weights) {

        assert total.compareTo(BigDecimal.ZERO) >= 0 && weights != null && weights.length > 0;

        if (total.compareTo(BigDecimal.ZERO) == 0) {
            BigDecimal[] result = new BigDecimal[weights.length];
            Arrays.fill(result, BigDecimal.ZERO);
            return result;
        }

        BigDecimal totalWeight = BigDecimal.ZERO;

        for (BigDecimal weight : weights) {
            totalWeight = totalWeight.add(weight);
        }

        BigDecimal[] result = new BigDecimal[weights.length];

        BigDecimal sum = BigDecimal.ZERO;

        int lastNotZeroIndex = -1;
        for (int i = 0; i < weights.length - 1; i++) {

            // MathContext.DECIMAL32 参数用于防止无限循环小数导致的异常
            result[i] = weights[i].divide(totalWeight, MathContext.DECIMAL32)
                    .multiply(total)
                    // 小数点后2位的直接舍去
                    .setScale(2, RoundingMode.DOWN);
            sum = sum.add(result[i]);

            if (weights[i].compareTo(BigDecimal.ZERO) > 0) {
                lastNotZeroIndex = i;
            }

        }

        if (weights[weights.length - 1].compareTo(BigDecimal.ZERO) == 0) {
            result[lastNotZeroIndex] = result[lastNotZeroIndex].add(total.subtract(sum));
            result[weights.length - 1] = BigDecimal.ZERO;
        } else {
            result[weights.length - 1] = total.subtract(sum);
        }

        return result;

    }

    public static BigDecimal devideThenMultiply(BigDecimal dividend, BigDecimal divisor, BigDecimal multiplier) {
        return dividend.divide(divisor, MathContext.DECIMAL32).multiply(multiplier).setScale(2, RoundingMode.DOWN);
    }

    /**
     * 处理null问题
     *
     * @return
     */
    public static BigDecimal zeroIfNull(BigDecimal number) {
        BigDecimal zero = BigDecimal.ZERO;
        return optional(number, zero);
    }

    /**
     * 处理null问题
     *
     * @return
     */
    public static Double zeroIfNull(Double number) {
        Double zero = 0D;
        return optional(number, zero);
    }

    /**
     * 处理null问题
     *
     * @return
     */
    public static Integer zeroIfNull(Integer number) {
        Integer zero = 0;
        return optional(number, zero);
    }

    /**
     * 处理null问题
     *
     * @return
     */
    public static Float zeroIfNull(Float number) {
        Float zero = 0F;
        return optional(number, zero);
    }

    /**
     * 处理null问题
     *
     * @return
     */
    public static Long zeroIfNull(Long number) {
        Long zero = 0L;
        return optional(number, zero);
    }

    /**
     * 处理null问题
     *
     * @param b
     * @return
     */
    public static boolean falseIfNull(Boolean b)
    {
        return optional(b, false);
    }

    public static <T> T optional(T t, T defaultValue) {
        return Optional.fromNullable(t).or(defaultValue);
    }

    public static Date toDay( Date date ) {
        Date result = DateUtils.setHours(date, 0);
        result = DateUtils.setMinutes(result, 0);
        result = DateUtils.setSeconds(result, 0);
        result = DateUtils.setMilliseconds(result, 0);
        return result;
    }

    public static int daysBetween( Date beginDate, Date endDate) {
        return Days.daysBetween(new DateTime(toDay(beginDate)), new DateTime(toDay(endDate))).getDays();
    }
}
