package org.dynamic.datasource.listener;

import com.mongodb.*;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dynamic.datasource.config.DynamicDataSource;
import org.dynamic.datasource.config.mongo.MultiMongoTemplate;
import org.dynamic.datasource.model.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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
import java.util.*;

/**
 * @author: JiangWH
 * @date: 2023/10/11 18:32
 * @version: 1.0.0
 */
public class MongoBeanListener implements BeanPostProcessor {
    
    private final static Logger log = LoggerFactory.getLogger(MongoBeanListener.class);
    
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
        if(beanName.equals(dynamicProperty.getDefaultMongoSourceName())) {
            log.info("=======================查询到了MongoTemplate=======================" + beanName);
            return mongoTemplate(mongoDbFactory);
        } else if(beanName.equals(dynamicProperty.getDefaultDataSourceName())) {
            log.info("=======================查询到了默认数据源(DataSource)=======================" + beanName);
            dataSource = customDataSource((DataSource) bean);
            return dataSource;
        }
        return bean;
    }
    
    public DataSource customDataSource(DataSource defaultDataSource) {
        List<DataBaseInfo> dataBaseInfos = dataBaseInfoList(defaultDataSource);
        
        Map<Object, Object> targetDataSources = new HashMap<>();
        if (dataBaseInfos.size() > 0) {
            for (DataBaseInfo info : dataBaseInfos) {
                String id = info.getId();
                HikariConfig hikariConfig = new HikariConfig();
                BeanUtils.copyProperties(info, hikariConfig);
                hikariConfig.setJdbcUrl(info.getUrl());
                targetDataSources.put(id, new HikariDataSource(hikariConfig));
                log.info("租户[{}}数据库配置初始化完毕", id);
            }
        }
        return new DynamicDataSource(defaultDataSource, targetDataSources);
    }
    
    private List<DataBaseInfo> dataBaseInfoList(DataSource dataSource) {
        try {
            List<DataBaseInfo> result = new ArrayList<DataBaseInfo>();
            Connection conn = dataSource.getConnection();
            
            PreparedStatement ps = conn.prepareStatement(dynamicProperty.getTableInfo().getSql());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String driver = rs.getString(dynamicProperty.getTableInfo().getDriverClassNameColumn());
                if(StringUtils.isEmpty(driver)) {
                    continue;
                }
                
                DataBaseInfo dbSource = new DataBaseInfo();
                String id = rs.getString(dynamicProperty.getTableInfo().getId());
                String url = rs.getString(dynamicProperty.getTableInfo().getUrlColumn());
                dbSource.setId(id);
                dbSource.setDriverClassName(driver);
                dbSource.setUrl(url);
                dbSource.setUsername(rs.getString(dynamicProperty.getTableInfo().getUsernameColumn()));
                dbSource.setPassword(rs.getString(dynamicProperty.getTableInfo().getPasswordColumn()));
                String[] split = url.split("//")[1].split(":");
                dbSource.setHost(split[0]);
                dbSource.setPort(split[1].split("/")[0]);
                dbSource.setDatabaseName(url.split(":")[2].split("/")[0]);
                result.add(dbSource);
                DynamicSourceInfo.add(id);
                DynamicSourceInfo.add(id, dbSource);
            }
            rs.close();
            ps.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
    }
    
    private MongoTemplate mongoTemplate(MongoDbFactory main) {
        Map<String, MongoDbFactory> mongoDbFactoryMap = new HashMap<>();
        try {
            if(dataSource == null) {
                DataSourceConfig dataSourceConfig = dynamicProperty.getDataSourceConfig();
                HikariConfig hikariConfig = new HikariConfig();
                hikariConfig.setUsername(dataSourceConfig.getUsername());
                hikariConfig.setPassword(dataSourceConfig.getPassword());
                hikariConfig.setDriverClassName(dataSourceConfig.getDriverClassName());
                hikariConfig.setJdbcUrl(dataSourceConfig.getUrl());
                dataSource = new HikariDataSource(hikariConfig);
            }
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
    
                MongoDataBaseInfo info = new MongoDataBaseInfo();
                info.setId(rs.getString(mongoInfo.getId()));
                info.setDatabaseName(mongoDataBaseName);
                info.setMongoProDatabase(rs.getString(mongoInfo.getMongoProDatabase()));
                info.setHost(rs.getString(mongoInfo.getHostColumn()));
                info.setPort(rs.getString(mongoInfo.getPortColumn()));
                info.setUsername(rs.getString(mongoInfo.getUsernameColumn()));
                info.setPassword(rs.getString(mongoInfo.getPasswordColumn()));
                info.setMongoAuthDatabase(rs.getString(mongoInfo.getAuthDataBaseColumn()));
                DynamicSourceInfo.add(rs.getString(mongoInfo.getId()), info);
                
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
