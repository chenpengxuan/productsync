package com.ymatou.productsync.infrastructure.util;

import com.ymatou.messagebus.client.KafkaBusClient;
import com.ymatou.messagebus.client.Message;
import com.ymatou.messagebus.client.MessageBusException;
import com.ymatou.productsync.infrastructure.constants.Constants;
import org.springframework.beans.factory.ObjectFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.util.UUID;

/**
 * Created by chenfei on 2017/2/9.
 * 消息总线分发
 */
public  class MessageBusDispatcher {

    @Resource
    private static KafkaBusClient kafkaBusClient;

    /**
     * 发送消息
     *
     * @param productId
     * @param actionType
     * @throws MessageBusException
     */
    public static void PublishAsync(String productId, String actionType) throws MessageBusException {

        Message req = new Message();
        req.setAppId(Constants.SNAPSHOP_MQ_ID);
        req.setCode(Constants.SNAPSHOP_MQ_CODE);
        req.setMessageId(UUID.randomUUID().toString());
        req.setBody(new MessageBusDto() {{
            setAppId(Constants.APP_ID);
            setProductId(productId);
            setActionType(actionType);
        }});
        kafkaBusClient.sendMessage(req);
    }

}

