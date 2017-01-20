package com.ymatou.productsync.infrastructure;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.google.common.base.Optional;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * Created by zhangyifan on 2016/12/14.
 */
public class Utils {
    /**
     * 默认格式
     */
    public static final String DEFAULT_DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    private static final Logger LOGGER = LoggerFactory.getLogger(Utils.class);

    private static volatile String localIp;

    public static String uuid() {
        return new ObjectId().toHexString();
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
    public static boolean falseIfNull(Boolean b) {
        return optional(b, false);
    }

    private static <T> T optional(T t, T defaultValue) {
        return Optional.fromNullable(t).or(defaultValue);
    }

    /**
     * 本机IP用于抢占定时任务，必须获取成功
     *
     * @return
     */
    public static String localIp() {
        if (localIp != null) {
            return localIp;
        }
        synchronized (Utils.class) {
            if (localIp == null) {
                try {
                    Enumeration<NetworkInterface> netInterfaces = NetworkInterface
                            .getNetworkInterfaces();

                    while (netInterfaces.hasMoreElements() && localIp == null) {
                        NetworkInterface ni = netInterfaces.nextElement();
                        if (!ni.isLoopback() && ni.isUp() && !ni.isVirtual()) {
                            Enumeration<InetAddress> address = ni.getInetAddresses();

                            while (address.hasMoreElements() && localIp == null) {
                                InetAddress addr = address.nextElement();

                                if (!addr.isLoopbackAddress() && addr.isSiteLocalAddress()
                                        && !(addr.getHostAddress().indexOf(":") > -1)) {
                                    localIp = addr.getHostAddress();

                                }
                            }
                        }
                    }

                } catch (Throwable t) {
                    throw new RuntimeException("Failed to fetch local ip", t);
                }
            }

            if (localIp == null || "127.0.0.1".equals(localIp)) {
                throw new RuntimeException("Failed to fetch local ip:" + localIp);
            }

            return localIp;
        }
    }

    /**
     * 对象转json字符串
     *
     * @param object
     * @return
     */
    public static String toJSONString(Object object) {
        JSON.DEFFAULT_DATE_FORMAT = DEFAULT_DATE_FORMAT;
        return JSON.toJSONString(object, SerializerFeature.WriteDateUseDateFormat, SerializerFeature.SortField);
    }
}
