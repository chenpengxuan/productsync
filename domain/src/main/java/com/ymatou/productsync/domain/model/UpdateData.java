package com.ymatou.productsync.domain.model;

import com.ymatou.productsync.facade.model.PrintFriendliness;

import java.util.List;
import java.util.Map;

/**
 * Created by chenpengxuan on 2017/1/19.
 */
public class UpdateData extends PrintFriendliness {
    /**
     * 需要进行同步的数据
     * 第一层List:多个表
     * 第二层List:标的多个行
     * 第三层:表的行值
     */
    //// FIXME: 2017/1/19 这里还需要添加关于直播的相关操作和相关数据结构 参照Products 如果要用灵活的方式，建议完全使用 List<List<Map<String, Object>>>这种方式
    private List<List<Map<String, Object>>> updateData;

    public UpdateData(List<List<Map<String, Object>>> updateData) {
        this.updateData = updateData;
    }

    public List<List<Map<String, Object>>> getUpdateData() {
        return updateData;
    }

}