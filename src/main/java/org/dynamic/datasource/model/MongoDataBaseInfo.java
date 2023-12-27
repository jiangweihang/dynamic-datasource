package org.dynamic.datasource.model;

/**
 * @author: JiangWH
 * @date: 2023/10/9 18:20
 * @version: 1.0.0
 */
public class MongoDataBaseInfo {
    
    /**
     * unique id
     */
    private String id;
    
    private String databaseName;
    
    private String host;
    
    private String port;
    
    private String username;
    
    private String password;
    
    private String mongoAuthDatabase;
    
    private String mongoProDatabase;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDatabaseName() {
        return databaseName;
    }
    
    public void setDatabaseName(String databaseName) {
        this.databaseName = databaseName;
    }
    
    public String getHost() {
        return host;
    }
    
    public void setHost(String host) {
        this.host = host;
    }
    
    public String getPort() {
        return port;
    }
    
    public void setPort(String port) {
        this.port = port;
    }
    
    public String getUsername() {
        return username;
    }
    
    public void setUsername(String username) {
        this.username = username;
    }
    
    public String getPassword() {
        return password;
    }
    
    public void setPassword(String password) {
        this.password = password;
    }
    
    public String getMongoAuthDatabase() {
        return mongoAuthDatabase;
    }
    
    public void setMongoAuthDatabase(String mongoAuthDatabase) {
        this.mongoAuthDatabase = mongoAuthDatabase;
    }
    
    public String getMongoProDatabase() {
        return mongoProDatabase;
    }
    
    public void setMongoProDatabase(String mongoProDatabase) {
        this.mongoProDatabase = mongoProDatabase;
    }
}
