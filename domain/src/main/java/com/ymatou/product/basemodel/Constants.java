/*
 *
 *  (C) Copyright 2016 Ymatou (http://www.ymatou.com/).
 *  All rights reserved.
 *
 */

package com.ymatou.product.basemodel;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;
import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author luoshiqian 2016/5/11 11:47
 */
public class Constants {

    public static final String APP_ID = "productsync.iapi.ymatou.com";

    public static final String LOG_PREFIX = "logPrefix";
    public static final DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    public static final DateTimeFormatter FORMATTER_YYYYMMDD = DateTimeFormat.forPattern("yyyy-MM-dd");
    public static final int MAX_PER_QUERY_COUNT = 1000;
    public static ConfigurableApplicationContext ctx;



    public static DateTime formatYMDDate(DateTime dateTime) {
        return DateTime.parse(dateTime.toString(Constants.FORMATTER_YYYYMMDD) + " 00:00:00", FORMATTER_YYYYMMDDHHMMSS);
    }
}
