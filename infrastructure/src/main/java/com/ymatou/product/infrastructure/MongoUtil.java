package com.ymatou.product.infrastructure;

import com.mongodb.DB;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.WriteResult;
import org.jongo.Jongo;
import org.jongo.MongoCollection;
import org.springframework.util.StringUtils;

/**
 * mongo 数据操作工具类
 * Created by chenpengxuan on 2017/1/17.
 */
public class MongoUtil {
    /**
     * mongo客户端
     */
    private static MongoClient client;

    /**
     * jongo类库
     */
    private static Jongo jongoClient;

    private MongoUtil(){}

    /**
     * 获取jongo实例
     * @param mongoUri
     * @param mongoDbName
     * @return
     */
    public static Jongo createInstance(String mongoUri,String mongoDbName) throws IllegalArgumentException{
        if(StringUtils.isEmpty(mongoUri) || StringUtils.isEmpty(mongoDbName)){
            throw new IllegalArgumentException("mongoUri或者mongoDbName不能为空");
        }
        if(client == null){
            MongoClientURI uri = new MongoClientURI(mongoUri);
            client = new MongoClient(uri);
            DB db = client.getDB(mongoDbName);
            jongoClient = new Jongo(db);
        }
        return jongoClient;
    }

    /**
     * 获取mongoCollection
     * @param tableName
     * @return
     * @throws Exception
     */
    public static MongoCollection getMongoCollection(String tableName) throws Exception{
        if(StringUtils.isEmpty(tableName)){
            throw new IllegalArgumentException("tableName不能为空");
        }
        if(jongoClient == null){
            throw new Exception("jongo实例为空，请先使用createInstance获取jongo实例");
        }
        return jongoClient.getCollection(tableName);
    }

    /**
     * save
     * @param object
     * @param tableName 如果表名为空或者空字符串，则将对象的名称作为表名
     * @throws Exception
     */
    public static WriteResult save(Object object, String tableName) throws Exception{
        MongoCollection collection = null;
        if(object == null){
            throw new IllegalArgumentException("要创建或更新的对象不能为空");
        }
        if(StringUtils.isEmpty(tableName)){
            collection = getMongoCollection(object.getClass().getSimpleName());
        }else{
            collection = getMongoCollection(tableName);
        }
        return collection.save(object);
    }

//    public static WriteResult save(Ar)
}
