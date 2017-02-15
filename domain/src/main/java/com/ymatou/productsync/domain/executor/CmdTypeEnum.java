package com.ymatou.productsync.domain.executor;

/**
 * 业务指令枚举，
 * 注意！！直播部分和商品部分分别添加到各自区域
 * Created by chenpengxuan on 2017/1/19.
 */
public enum CmdTypeEnum {
//    商品部分
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
     * 上架商品【商品售罄】
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
    ModifyActivityPrice,

//    直播部分
    /**
     *添加直播
     */
    AddActivity,

    /**
     * 关闭直播
     */
    CloseActivity,

    /**
     * 确认直播
     */
    ConfirmActivity,

    /**
     * 创建直播
     */
    CreateActivity,

    /**
     * 删除直播
     */
    DeleteActivity,

    /**
     * 修改直播
     */
    ModifyActivity,

    /**
     * 保存直播信息
     */
    SaveActivity,

    /**
     * 修改直播
     */
    UpdateActivity,

    /**
     * 更新买手直播名称
     */
    UpdateActivityName,

    /**
     * 修改直播商品排序
     */
    UpdateActivitySort,

    /**
     * 同步活动商品
     */
    SyncActivityProduct
    ;
}
