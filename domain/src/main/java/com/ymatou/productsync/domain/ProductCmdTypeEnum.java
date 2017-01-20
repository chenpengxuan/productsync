package com.ymatou.productsync.domain;

/**
 * 业务指令枚举
 * Created by chenpengxuan on 2017/1/19.
 */
public enum ProductCmdTypeEnum {
    /**
     * 添加商品（批量）,关联商品和直播
     */
    AddProduct,

    /**
     * 修改上架商品信息
     */
    ModifyPutawayProductInfo,

    /**
     * 修改商品待上架信息(批量)
     */
    ModifyReadyPutawayProductInfo,

    /**
     * 批量商品上架 PC
     */
    BatchSetOnShelf,

    /**
     * 批量上架商品 App
     */
    SetOnShelfUpdateStockNum,

    /**
     * 添加商品图片
     */
    AddProductPics,

    /**
     * 删除某个商品下的图片(单选/多选)
     */
    DeleteProductPics,

    /**
     *  删除商品
     */
    DeleteProduct,

    /**
     * 商品下架
     */
    ProductPutout,

    /**
     * 直播商品暂停销售
     */
    SuspendSale,

    /**
     * 移出直播
     */
    RemoveFromActivity,

    /**
     * 设置推荐置顶顶商品
     */
    SetOnTop,

    /**
     * 设置取消推荐置顶商品
     */
    SetOffTop,

    /**
     * 设置、取消橱窗商品
     */
    SetTopProduct,

    /**
     * 修改商品描述、图片
     */
    UpdateProductsInfo,

    /**
     * 商品库存增减,价格修改
     */
    ProductStockChange,

    /**
     * 修改分类品牌
     */
    ModifyBrandAndCategory,

    /**
     * 自动上架
     */
    AutoOnShelf,

    /**
     * 交易更新商品规格库存
     */
    CatalogStockChange,

    /**
     * 交易更新商品活动库存
     */
    ActivityStockChange,

    /**
     * 活动审核调拨活动库存
     */
    ActivityStockTransfer,

    /**
     * 修改商品活动价，老库是查询的，无需修改
     */
    ModifyActivityPrice,;
}
