package org.dynamic.datasource.config.mongo;

import com.mongodb.DB;
import org.dynamic.datasource.core.DataSourceContextHolder;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;

import java.util.Map;

/**
 * @author: JiangWH
 * @date: 2023/10/10 16:01
 * @version: 1.0.0
 */
public class MultiMongoTemplate extends MongoTemplate {
    
    private final MongoDbFactory DEFAULT_MONGO_FACTORY;
    
    private final Map<String, MongoDbFactory> TARGET_MONGO_FACTORY;
    
    public MultiMongoTemplate(MongoDbFactory defaultFactory, Map<String, MongoDbFactory> targetFactory) {
        super(defaultFactory);
        this.DEFAULT_MONGO_FACTORY = defaultFactory;
        this.TARGET_MONGO_FACTORY = targetFactory;
    }
    
    @Override
    public DB getDb() {
        String id = DataSourceContextHolder.getId();
        if(id == null) {
            return DEFAULT_MONGO_FACTORY.getDb();
        }
        MongoDbFactory mongoDbFactory = TARGET_MONGO_FACTORY.get(id);
        if(mongoDbFactory == null) {
            return DEFAULT_MONGO_FACTORY.getDb();
        }
        return mongoDbFactory.getDb();
    }
    
}
