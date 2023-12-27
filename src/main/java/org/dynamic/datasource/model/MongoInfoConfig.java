package org.dynamic.datasource.model;

/**
 * 对应存储mongodb连接信息的字段
 * @author: JiangWH
 * @date: 2023/10/10 15:16
 * @version: 1.0.0
 */
public class MongoInfoConfig {
    
    private String sql;
    
    private String id;
    
    private String databaseColumn;
    
    private String mongoProDatabase;
    
    private String hostColumn;
    
    private String portColumn;
    
    private String usernameColumn;
    
    private String passwordColumn;
    
    private String authDataBaseColumn;
    
    public String getSql() {
        return sql;
    }
    
    public void setSql(String sql) {
        this.sql = sql;
    }
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDatabaseColumn() {
        return databaseColumn;
    }
    
    public void setDatabaseColumn(String databaseColumn) {
        this.databaseColumn = databaseColumn;
    }
    
    public String getHostColumn() {
        return hostColumn;
    }
    
    public void setHostColumn(String hostColumn) {
        this.hostColumn = hostColumn;
    }
    
    public String getPortColumn() {
        return portColumn;
    }
    
    public void setPortColumn(String portColumn) {
        this.portColumn = portColumn;
    }
    
    public String getUsernameColumn() {
        return usernameColumn;
    }
    
    public void setUsernameColumn(String usernameColumn) {
        this.usernameColumn = usernameColumn;
    }
    
    public String getPasswordColumn() {
        return passwordColumn;
    }
    
    public void setPasswordColumn(String passwordColumn) {
        this.passwordColumn = passwordColumn;
    }
    
    public String getAuthDataBaseColumn() {
        return authDataBaseColumn;
    }
    
    public void setAuthDataBaseColumn(String authDataBaseColumn) {
        this.authDataBaseColumn = authDataBaseColumn;
    }
    
    public String getMongoProDatabase() {
        return mongoProDatabase;
    }
    
    public void setMongoProDatabase(String mongoProDatabase) {
        this.mongoProDatabase = mongoProDatabase;
    }
}
