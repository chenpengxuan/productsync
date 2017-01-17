package com.ymatou.product.basemodel;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import java.io.Serializable;
import java.util.Date;

/**
 * 用于打印日志
 * Created by chenpengxuan on 2016/9/6.
 */
public class BaseInfo implements Serializable {

    private static final long serialVersionUID = -235822080790001901L;
    private static final DateTimeFormatter FORMATTER_YYYYMMTDDHHMMSS = DateTimeFormat.forPattern("yyyy-MM-dd'T'HH:mm:ss");
    private static final DateTimeFormatter FORMATTER_YYYYMMDDHHMMSS = DateTimeFormat.forPattern("yyyy-MM-dd HH:mm:ss");
    @Override
    public String toString() {
        return this.getClass().getSimpleName() + ":"
                + JSON.toJSONString(this, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.SkipTransientField);
    }

    protected Date convertDotNetDateToDate(String value) {
        Date date = null;
        if (value != null) {
            try {
                date = DateTime.parse(value, FORMATTER_YYYYMMTDDHHMMSS).toDate();
            } catch (Exception e) {
                try {
                    date = DateTime.parse(value, FORMATTER_YYYYMMDDHHMMSS).toDate();
                } catch (Exception ex) {

                }
            }
        }
        return date;
    }
}