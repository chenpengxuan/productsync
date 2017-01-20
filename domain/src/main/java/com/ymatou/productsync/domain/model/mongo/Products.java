package com.ymatou.productsync.domain.model.mongo;

import com.ymatou.productsync.domain.model.annotation.Column;
import com.ymatou.productsync.domain.model.annotation.Table;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;

/**
 * 商品主体信息汇总
 * Created by chenpengxuan on 2017/1/19.
 */
public class Products {
    /**
     * 商品id
     */
    @Table("Ymt_Products")
    @Column("sProductId")
    @Id
    @Field("spid")
    private String productId;

    /**
     * 商品数字id
     */
    @Table("Ymt_Products")
    @Column("ipid")
    @Field("ProdId")
    private int pId;

    /**
     * 商品名称
     */
    @Table("Ymt_Products")
    @Column("sProduct")
    @Field("title")
    private String title;

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public int getpId() {
        return pId;
    }

    public void setpId(int pId) {
        this.pId = pId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Products)) return false;

        Products products = (Products) o;

        if (pId != products.pId) return false;
        if (!productId.equals(products.productId)) return false;
        return title.equals(products.title);

    }

    @Override
    public int hashCode() {
        int result = productId.hashCode();
        result = 31 * result + pId;
        result = 31 * result + title.hashCode();
        return result;
    }
}
