package org.dynamic.datasource.model;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * @author: JiangWH
 * @date: 2023/10/10 15:03
 * @version: 1.0.0
 */
@Component
@ConfigurationProperties(prefix = "custom.dynamic")
public class DynamicProperty {
    
    private TableInfoConfig tableInfo;
    
    private AspectConfig aspect;
    
    private MongoInfoConfig mongoInfo;
    
    private IdInfo idInfo;
    
    private String defaultDataSourceName;
    
    private String defaultMongoSourceName;
    
    private DataSourceConfig dataSourceConfig;
    
    public TableInfoConfig getTableInfo() {
        return tableInfo;
    }
    
    public void setTableInfo(TableInfoConfig tableInfo) {
        this.tableInfo = tableInfo;
    }
    
    public AspectConfig getAspect() {
        return aspect;
    }
    
    public void setAspect(AspectConfig aspect) {
        this.aspect = aspect;
    }
    
    public MongoInfoConfig getMongoInfo() {
        return mongoInfo;
    }
    
    public void setMongoInfo(MongoInfoConfig mongoInfo) {
        this.mongoInfo = mongoInfo;
    }
    
    public IdInfo getIdInfo() {
        return idInfo;
    }
    
    public void setIdInfo(IdInfo idInfo) {
        this.idInfo = idInfo;
    }
    
    public String getDefaultDataSourceName() {
        return defaultDataSourceName;
    }
    
    public void setDefaultDataSourceName(String defaultDataSourceName) {
        this.defaultDataSourceName = defaultDataSourceName;
    }
    
    public String getDefaultMongoSourceName() {
        return defaultMongoSourceName;
    }
    
    public void setDefaultMongoSourceName(String defaultMongoSourceName) {
        this.defaultMongoSourceName = defaultMongoSourceName;
    }
    
    public DataSourceConfig getDataSourceConfig() {
        return dataSourceConfig;
    }
    
    public void setDataSourceConfig(DataSourceConfig dataSourceConfig) {
        this.dataSourceConfig = dataSourceConfig;
    }
}
