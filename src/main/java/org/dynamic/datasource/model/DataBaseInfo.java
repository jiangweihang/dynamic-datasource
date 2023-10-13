package org.dynamic.datasource.model;

/**
 * @author: JiangWH
 * @date: 2023/10/9 18:20
 * @version: 1.0.0
 */
public class DataBaseInfo {
    
    /**
     * unique id
     */
    private String id;
    
    /**
     * 驱动名称
     */
    private String driverClassName;
    
    /**
     * 数据库连接URL
     */
    private String url;
    
    /**
     * 用户名
     */
    private String username;
    
    /**
     * 密码
     */
    private String password;
    
    public String getId() {
        return id;
    }
    
    public void setId(String id) {
        this.id = id;
    }
    
    public String getDriverClassName() {
        return driverClassName;
    }
    
    public void setDriverClassName(String driverClassName) {
        this.driverClassName = driverClassName;
    }
    
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
}
