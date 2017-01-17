package com.ymatou.product.basemodel;

import org.springframework.util.StringUtils;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Set;

/**
 * SDK 请求响应基本模型
 * Created by chenpengxuan on 2016/9/5.
 */
public abstract class BaseRequest extends BaseInfo {
    private static final long serialVersionUID = -7224533704721037782L;

    private static Validator VALIDATOR = Validation.buildDefaultValidatorFactory().getValidator();
    /**
     * 直播id
     */
    protected int liveId;

    /**
     * 商品id
     */
    protected String productId;

    /**
     * 业务场景指令
     */
    @NotNull
    protected String actionType;


    /**
     * 业务凭据id
     */
    @Min(value = 1,message = "业务凭据id不能为空")
    protected int transactionId;

    public int getLiveId() {
        return liveId;
    }

    public void setLiveId(int liveId) {
        this.liveId = liveId;
    }

    public String getProductId() {
        return productId;
    }

    public void setProductId(String productId) {
        this.productId = productId;
    }

    public String getActionType() {
        return actionType;
    }

    public void setActionType(String actionType) {
        this.actionType = actionType;
    }

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    /**
     * 所有子类必须对参数进行边界检查
     */
   public void Validate() throws ApiException{
       StringBuilder errorMsgs = new StringBuilder();
       if(StringUtils.isEmpty(productId) && liveId == 0){
           throw new ApiException(ErrorCode.ILLEGAL_ARGUMENT,"商品id与直播id不能都为空");
       }
       Set<ConstraintViolation<BaseRequest>> violations = VALIDATOR.validate(this);
       if (violations != null && violations.size() > 0) {
           for (ConstraintViolation<BaseRequest> violation : violations) {
               errorMsgs.append(violation.getPropertyPath()).append(":").append(violation.getMessage()).append("|");
           }
           throw new ApiException(ErrorCode.ILLEGAL_ARGUMENT,errorMsgs.substring(0, errorMsgs.length() - 1));
       }
   }
}
