package com.ymatou.productsync.infrastructure.config.datasource;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.ymatou.productsync.infrastructure.constants.Constants;
import com.ymatou.productsync.infrastructure.util.LogWrapper;
import com.ymatou.productsync.infrastructure.util.Utils;
import org.jongo.marshall.jackson.configuration.MapperModifier;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.math.BigDecimal;

/**
 * Mongo 数据映射
 * Created by chenpengxuan on 2017/3/7.
 */
@Component
public class MongoDataMappingConverter implements MapperModifier{
    @Autowired
    LogWrapper logWrapper;

    @Override
    public void modify(ObjectMapper objectMapper){
        SimpleModule tempMoudle = new SimpleModule(
                "CustomDeserializer",
                new Version(1, 0, 0, null,"com.ymatou", Constants.APP_ID))
                .addSerializer(BigDecimal.class, new JsonSerializer<BigDecimal>() {
                    @Override
                    public void serialize(BigDecimal decimal, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException, JsonProcessingException {
                        objectMapper.writeValue(jsonGenerator,Utils.decimalFormat(decimal,2));
                    }
                });
        objectMapper.registerModule(tempMoudle);
    }
}
