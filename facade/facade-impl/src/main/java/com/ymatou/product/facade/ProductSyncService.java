package com.ymatou.product.facade;

import com.alibaba.dubbo.config.annotation.Service;
import com.ymatou.product.basemodel.BaseRequest;
import com.ymatou.product.basemodel.BaseResponse;
import com.ymatou.product.basemodel.ProductSyncRequest;
import com.ymatou.product.domain.ActionTypeEnum;
import com.ymatou.product.service.IProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.ws.rs.Consumes;
import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

/**
 * Created by chenpengxuan on 2017/1/16.
 */
@Service(protocol = { "rest", "dubbo" })
@Component
@Path("/{api:(?i:api)}")
@Consumes({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
@Produces({MediaType.APPLICATION_JSON, MediaType.APPLICATION_XML})
public class ProductSyncService implements IProductSyncService {
    @Autowired
    private IProductService productService;
    @GET
    @Path("/{cache:(?i:cache)}/{mongocrud:(?:mongocrud)}")
    @Override
    public BaseResponse productSync(ProductSyncRequest request) {
         BaseResponse response = new
        switch (ActionTypeEnum.findByActionType(request.getActionType())){
            case DeleteProduct:
                return productService.DeleteProduct(request.getLiveId(),request.getProductId(),request.getTransactionId());
                break;
        }
        return null;
    }
}
