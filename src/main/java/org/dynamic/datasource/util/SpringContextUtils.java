package org.dynamic.datasource.util;

import org.springframework.context.ConfigurableApplicationContext;

/**
 * @author: JiangWH
 * @date: 2023/10/11 16:44
 * @version: 1.0.0
 */
public class SpringContextUtils {
    
    private static ConfigurableApplicationContext context;
    
    private SpringContextUtils() {}
    
    public static ConfigurableApplicationContext getContext() {
        return context;
    }
    
    public static synchronized void initContext(ConfigurableApplicationContext context) {
        if(SpringContextUtils.context == null) {
            SpringContextUtils.context = context;
        }
    }
    
    public static <T> T getBean(String name) {
        return (T) context.getBean(name);
    }
    
    public static <T> T getBean(String name, Class<T> clazz) {
        return (T) context.getBean(name, clazz);
    }
    
    public static <T> T getBean(Class<T> clazz) {
        return context.getBean(clazz);
    }
    
}
