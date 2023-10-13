package org.dynamic.datasource.model;

/**
 * 数据源所存在的表
 * @author: JiangWH
 * @date: 2023/10/9 18:45
 * @version: 1.0.0
 */
public class TableInfoConfig {
    
    private String sql;
    
    private String id;
    
    /**
     * 驱动名称
     */
    private String driverClassNameColumn;
    
    /**
     * 数据库连接URL
     */
    private String urlColumn;
    
    /**
     * 用户名
     */
    private String usernameColumn;
    
    /**
     * 密码
     */
    private String passwordColumn;
    
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
    
    public String getDriverClassNameColumn() {
        return driverClassNameColumn;
    }
    
    public void setDriverClassNameColumn(String driverClassNameColumn) {
        this.driverClassNameColumn = driverClassNameColumn;
    }
    
    public String getUrlColumn() {
        return urlColumn;
    }
    
    public void setUrlColumn(String urlColumn) {
        this.urlColumn = urlColumn;
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
    
}
