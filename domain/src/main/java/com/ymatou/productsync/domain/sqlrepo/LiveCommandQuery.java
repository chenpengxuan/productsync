package com.ymatou.productsync.domain.sqlrepo;

import com.ymatou.productsync.infrastructure.config.datasource.TargetDataSource;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

/**
 * sql数据查询相关
 * Created by zhangyong on 2017/2/7.
 */
public interface LiveCommandQuery {
    /**
     * 获取直播相关信息
     * @param ActivityId
     * @return
     */
    @TargetDataSource("liveDataSource")
    List<List<Map<String,Object>>> getActivityInfo(@Param("ActivityId") int ActivityId);
}
