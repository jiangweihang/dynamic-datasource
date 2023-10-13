package org.dynamic.datasource.model;

/**
 * @author: JiangWH
 * @date: 2023/10/13 16:40
 * @version: 1.0.0
 */
public class DataSourceConfig {

    private String url;
    
    private String username;
    
    private String password;
    
    private String driverClassName;
    
    public String getUrl() {
        return url;
    }
    
    public void setUrl(String url) {
        this.url = url;
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
    
    public String getDriverClassName() {
        return driverClassName;
    }
    
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
}
