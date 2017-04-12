package com.ymatou.productsync.domain.model.mongo;

import java.util.List;

/**
 * 商品同步信息变更边界信息
 * Created by chenpengxuan on 2017/3/21.
 */
public class ProductChangedRange {
    /**
     * 变更涉及到的商品id
     */
    private List<String> productIdList;

    /**
     * 变更涉及到的范围
     */
    private List<String> productTableRangeList;

    public List<String> getProductIdList() {
        return productIdList;
    }

    public void setProductIdList(List<String> productIdList) {
        this.productIdList = productIdList;
    }

    public List<String> getProductTableRangeList() {
        return productTableRangeList;
    }

    public void setProductTableRangeList(List<String> productTableRangeList) {
        this.productTableRangeList = productTableRangeList;
    }
}
