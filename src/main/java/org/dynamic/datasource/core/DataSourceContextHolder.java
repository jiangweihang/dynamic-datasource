package org.dynamic.datasource.core;

/**
 * @author: JiangWH
 * @date: 2023/10/9 19:17
 * @version: 1.0.0
 */
public class DataSourceContextHolder {
    
    /**
     * 使用ThreadLocal维护变量，ThreadLocal为每个使用该变量的线程提供独立的变量副本，
     * 所以每一个线程都可以独立地改变自己的副本，而不会影响其它线程所对应的副本。
     */
    private static final ThreadLocal<String> CONTEXT_HOLDER = new ThreadLocal<>();
    
    public static String getId() {
        return CONTEXT_HOLDER.get();
    }
    
    public static void setId(String id) {
        CONTEXT_HOLDER.set(id);
    }
    
    public static void clearId() {
        CONTEXT_HOLDER.remove();
    }
    
}
