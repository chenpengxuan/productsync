package com.ymatou.productsync.infrastructure.util;

import com.alibaba.fastjson.JSON;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.ymatou.productsync.infrastructure.util.Utils.DEFAULT_DATE_FORMAT;

/**
 * Map Util
 * Created by chenpengxuan on 2017/2/7.
 */
public class MapUtil {
    /**
     * 将mapList转换成json字符串格式 例如：{xx:oo},{yy:xx}
     *
     * @param mapList
     * @return
     * @throws IllegalArgumentException
     */
    public static String makeJsonStringFromMap(List<Map<String, Object>> mapList) throws IllegalArgumentException {
        if (mapList == null || mapList.isEmpty())
            throw new IllegalArgumentException("mongo 待操作数据不能为空");
        return String.join(",", mapList.stream().map(x -> makeJsonStringFromMap(x)).toArray(String[]::new));
    }

    /**
     * 将map转换成json字符串格式
     *
     * @return
     */
    public static String makeJsonStringFromMap(Map<String, Object> map) throws IllegalArgumentException {
        if (map == null || map.isEmpty())
            throw new IllegalArgumentException("mongo 待操作数据不能为空");
        return JSON.toJSONString(map);
    }

    /**
     * 将map转换成obj格式
     *
     * @param map
     * @return
     * @throws IllegalArgumentException
     */
    public static Object makeObjFromMap(Map<String, Object> map) throws IllegalArgumentException {
        if (map == null || map.isEmpty())
            throw new IllegalArgumentException("mongo 待操作数据不能为空");
        return JSON.toJSON(map);
    }

    /**
     * 将map转换成obj格式
     *
     * @param mapList
     * @return
     * @throws IllegalArgumentException
     */
    public static Object[] makeObjFromMap(List<Map<String, Object>> mapList) throws IllegalArgumentException {
        if (mapList == null || mapList.isEmpty())
            throw new IllegalArgumentException("mongo 待操作数据不能为空");
        JSON.DEFFAULT_DATE_FORMAT = DEFAULT_DATE_FORMAT;
        return mapList.stream().map(x -> JSON.toJSON(x)).toArray();
    }

    /**
     * Map转换为list
     *
     * @param map
     * @return
     */
    public static List<Map<String, Object>> MapToList(Map<String, Object> map) {
        return Stream.of(map).collect(Collectors.toList());
    }


    /**
     * 将maplist中的字符串转换为数组
     *
     * @param mapList
     * @param field
     * @param seperator
     */
    public static void MapFieldToStringArray(List<Map<String, Object>> mapList, String field, String seperator) {
        if (mapList == null) return;
        Map<String, Object> map = mapList.stream().findFirst().orElse(Collections.emptyMap());
        if (map != null) {
            map.replace(field, map.get(field), map.get(field).toString().split(seperator));
        }
    }
}
