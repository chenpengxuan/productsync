package com.ymatou.product.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * 业务场景枚举
 * Created by chenpengxuan on 2017/1/16.
 */
public enum  ActionTypeEnum {
    /**
     * 添加商品（批量）,关联商品和直播
     */
    AddProduct("AddProduct"),

    /**
     * 修改上架商品信息
     */
    ModifyPutawayProductInfo("ModifyPutawayProductInfo"),

    /**
     * 修改商品待上架信息(批量)
     */
    ModifyReadyPutawayProductInfo("ModifyReadyPutawayProductInfo"),

    /**
     * 批量商品上架 PC
     */
    BatchSetOnShelf("BatchSetOnShelf"),

    /**
     * 批量上架商品 App
     */
    SetOnShelfUpdateStockNum("SetOnShelfUpdateStockNum"),

    /**
     * 添加商品图片
     */
    AddProductPics("AddProductPics"),

    /**
     * 删除某个商品下的图片(单选/多选)
     */
    DeleteProductPics("DeleteProductPics"),

    /**
     *  删除商品
     */
    DeleteProduct("DeleteProduct"),

    /**
     * 商品下架
     */
    ProductPutout("ProductPutout"),

    /**
     * 直播商品暂停销售
     */
    SuspendSale("SuspendSale"),

    /**
     * 移出直播
     */
    RemoveFromActivity("RemoveFromActivity"),

    /**
     * 设置推荐置顶顶商品
     */
    SetOnTop("SetOnTop"),

    /**
     * 设置取消推荐置顶商品
     */
    SetOffTop("SetOffTop"),

    /**
     * 设置、取消橱窗商品
     */
    SetTopProduct("SetTopProduct"),

    /**
     * 修改商品描述、图片
     */
    UpdateProductsInfo("UpdateProductsInfo"),

    /**
     * 商品库存增减,价格修改
     */
    ProductStockChange("ProductStockChange"),

    /**
     * 修改分类品牌
     */
    ModifyBrandAndCategory("ModifyBrandAndCategory"),

    /**
     * 自动上架
     */
    AutoOnShelf("AutoOnShelf"),

    /**
     * 交易更新商品规格库存
     */
    CatalogStockChange("CatalogStockChange"),

    /**
     * 交易更新商品活动库存
     */
    ActivityStockChange("ActivityStockChange"),

    /**
     * 活动审核调拨活动库存
     */
    ActivityStockTransfer("ActivityStockTransfer"),

    /**
     * 修改商品活动价，老库是查询的，无需修改
     */
    ModifyActivityPrice("ModifyActivityPrice"),

    ;

    private String actionType;

    private static final Map<String,ActionTypeEnum> maps = new HashMap<>();

    ActionTypeEnum(String actionType) {
        this.actionType = actionType;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    static {
        for(ActionTypeEnum platformEnum : ActionTypeEnum.values()){
            maps.put(platformEnum.getActionType(),platformEnum);
        }
    }

    /**
     * 根据业务场景指令字符串返回业务场景枚举
     * @param actionTypeStr
     * @return
     */
    public static ActionTypeEnum findByActionType(String actionTypeStr){
        ActionTypeEnum actionType = maps.get(actionTypeStr);
        if (actionType == null) {
            throw new IllegalArgumentException("actiontypeenum not found,actiontype:" + actionType);
        }
        return actionType;
    }
}
