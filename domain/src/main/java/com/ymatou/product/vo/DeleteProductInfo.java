package com.ymatou.product.vo;

import org.joda.time.DateTime;

/**
 * 商品信息(for delete)
 * Created by chenpengxuan on 2017/1/17.
 */
public class DeleteProductInfo {
    /**
     * 有效期开始日期
     */
    private DateTime validStartTime;

    /**
     * 有效期结束日期
     */
    private DateTime vaildEndTime;

    /**
     * 是否新品
     */
    private boolean isNew;

    /**
     * 新品开始时间
     */
    private DateTime newStartTime;

    /**
     * 新品结束时间
     */
    private DateTime newEndTime;

    /**
     * 状态标志位 1表示有效 0表示逻辑删除
     */
    private int action;

    public DateTime getValidStartTime() {
        return validStartTime;
    }

    public void setValidStartTime(DateTime validStartTime) {
        this.validStartTime = validStartTime;
    }

    public DateTime getVaildEndTime() {
        return vaildEndTime;
    }

    public void setVaildEndTime(DateTime vaildEndTime) {
        this.vaildEndTime = vaildEndTime;
    }

    public boolean isNew() {
        return isNew;
    }

    public void setNew(boolean aNew) {
        isNew = aNew;
    }

    public DateTime getNewStartTime() {
        return newStartTime;
    }

    public void setNewStartTime(DateTime newStartTime) {
        this.newStartTime = newStartTime;
    }

    public DateTime getNewEndTime() {
        return newEndTime;
    }

    public void setNewEndTime(DateTime newEndTime) {
        this.newEndTime = newEndTime;
    }

    public int getAction() {
        return action;
    }

    public void setAction(int action) {
        this.action = action;
    }
}
