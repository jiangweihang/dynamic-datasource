package org.dynamic.datasource.listener;

import com.mongodb.*;
import org.dynamic.datasource.config.mongo.MultiMongoTemplate;
import org.dynamic.datasource.model.DynamicProperty;
import org.dynamic.datasource.model.MongoInfoConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.data.mongodb.MongoDbFactory;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.net.UnknownHostException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * @author: JiangWH
 * @date: 2023/10/11 18:32
 * @version: 1.0.0
 */
public class MongoBeanListener implements BeanPostProcessor {
    
    private final static Logger log = LoggerFactory.getLogger(MongoBeanListener.class);
    
    @Autowired
    private DataSource dataSource;
    
    @Autowired
    private MongoDbFactory mongoDbFactory;
    
    @Autowired
    private DynamicProperty dynamicProperty;
    
    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        return bean;
    }
    
    @Override
    public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if(bean instanceof MongoTemplate) {
            System.out.println("=======================查询到了MongoTemplate=======================" + beanName);
            return mongoTemplate(mongoDbFactory);
        }
        return bean;
    }
    
    private MongoTemplate mongoTemplate(MongoDbFactory main) {
        Map<String, MongoDbFactory> mongoDbFactoryMap = new HashMap<>();
        try {
            Connection conn = dataSource.getConnection();
            MongoInfoConfig mongoInfo = dynamicProperty.getMongoInfo();
            PreparedStatement ps = conn.prepareStatement(mongoInfo.getSql());
            ResultSet rs = ps.executeQuery();
            
            while (rs.next()) {
                String mongoDataBaseName = rs.getString(mongoInfo.getDatabaseColumn());
                if(StringUtils.isEmpty(mongoDataBaseName)) {
                    continue;
                }
                
                MongoClientOptions.Builder builder = new MongoClientOptions.Builder();
                builder.socketKeepAlive(true);
                builder.socketTimeout(300000);
                builder.maxWaitTime(360000);
                builder.connectTimeout(360000);
                builder.connectionsPerHost(120);
                builder.threadsAllowedToBlockForConnectionMultiplier(40);
                builder.maxConnectionIdleTime(1800000);
                builder.maxConnectionLifeTime(2400000);
                builder.readPreference(ReadPreference.primary());
                final MongoClientOptions options = builder.build();
                MongoCredential credential = MongoCredential.createCredential(rs.getString(mongoInfo.getUsernameColumn()),
                        rs.getString(mongoInfo.getAuthDataBaseColumn()), rs.getString(mongoInfo.getPasswordColumn()).toCharArray());
                MongoClient mongoClient = new MongoClient(new ServerAddress(rs.getString(mongoInfo.getHostColumn()),
                        rs.getInt(mongoInfo.getPortColumn())), Arrays.asList(credential),options);
                MongoDbFactory tempFactory = new SimpleMongoDbFactory(mongoClient, mongoDataBaseName);
                mongoDbFactoryMap.put(rs.getString(mongoInfo.getId()), tempFactory);
                
                log.info("Mongo数据源[{}]加载完毕.", rs.getString(mongoInfo.getId()));
            }
            rs.close();
            ps.close();
            conn.close();
        } catch (SQLException | UnknownHostException e) {
            throw new RuntimeException(e);
        }
        
        return new MultiMongoTemplate(main, mongoDbFactoryMap);
    }
    
}
