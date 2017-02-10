package com.ymatou.productsync.domain.executor;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by chenfei on 2017/2/7.
 * mongo参数创建
 */
public class MongoQueryBuilder {

    /**
     * 根据直播id查询
     *
     * @param liveId
     * @return
     */
    public static Map<String, Object> queryLiveId(long liveId) {
        Map<String, Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("lid", liveId);
        return matchConditionInfo;
    }

    /**
     * 根据商品id查询
     *
     * @param productId
     * @return
     */
    public static Map<String, Object> queryProductId(String productId) {
        Map<String, Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("spid", productId);
        return matchConditionInfo;
    }

    /**
     * 根据商品与直播id查询
     *
     * @param productId
     * @param liveId
     * @return
     */
    public static Map<String, Object> queryProductIdAndLiveId(String productId, long liveId) {
        Map<String, Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("spid", productId);
        matchConditionInfo.put("lid", liveId);
        return matchConditionInfo;
    }


    /**
     * 根据商品编号与活动编号查询
     *
     * @param productId
     * @param activityId
     * @return
     */
    public static Map<String, Object> queryProductIdAndActivityId(String productId, long activityId) {
        Map<String, Object> matchConditionInfo = new HashMap();
        matchConditionInfo.put("spid", productId);
        matchConditionInfo.put("aid", activityId);
        return matchConditionInfo;
    }
}
