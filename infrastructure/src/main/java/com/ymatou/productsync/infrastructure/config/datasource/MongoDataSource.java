package com.ymatou.productsync.infrastructure.config.datasource;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.ymatou.productsync.infrastructure.config.props.MongoProps;
import org.jongo.Jongo;
import org.jongo.marshall.jackson.JacksonMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.DependsOn;

/** mongo data source
 * Created by chenpengxuan on 2017/2/6.
 */
@Configuration
@DependsOn({"disconfMgrBean2"})
public class MongoDataSource {
    @Autowired
    private MongoProps mongoProps;

    @Autowired
    private MongoDataMappingConverter mongoDataMappingConverter;

    @Bean
    public Jongo jongoClient(){
        MongoClientURI uri = new MongoClientURI(mongoProps.getMongoProductUrl());
        //Todo getDB deprecated,please use getDatabase
        DB db =new MongoClient(uri).getDB(uri.getDatabase());
        return new Jongo(db,new JacksonMapper.Builder().addModifier(mongoDataMappingConverter).build());
    }
}
