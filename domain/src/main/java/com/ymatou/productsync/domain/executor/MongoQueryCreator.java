package com.ymatou.productsync.domain.executor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/7.
 * mongo参数创建
 */
public class MongoQueryCreator {

    public static Map<String,Object> CreateLiveId(long liveId){

        Map<String,Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("lid",liveId);
        return matchConditionInfo;
    }


    public static Map<String,Object> CreateProductId(String productId){

        Map<String,Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("spid",productId);
        return matchConditionInfo;
    }


    public static Map<String,Object> CreateByMapKey(String key,Map<String,Object> map){

        Map<String,Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put(key,map.get(key));
        return matchConditionInfo;
    }
}
