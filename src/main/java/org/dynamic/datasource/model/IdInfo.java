package org.dynamic.datasource.model;

/**
 * @author: JiangWH
 * @date: 2023/10/11 10:52
 * @version: 1.0.0
 */
public class IdInfo {
    
    private String queryIdClass;
    
    private String method;
    
    public String getQueryIdClass() {
        return queryIdClass;
    }
    
    public void setQueryIdClass(String queryIdClass) {
        this.queryIdClass = queryIdClass;
    }
    
    public String getMethod() {
        return method;
    }
    
    public void setMethod(String method) {
        this.method = method;
    }
}
