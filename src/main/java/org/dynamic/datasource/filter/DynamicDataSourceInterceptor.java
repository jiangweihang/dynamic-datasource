package org.dynamic.datasource.filter;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.dynamic.datasource.core.DataSourceContextHolder;
import org.dynamic.datasource.model.DynamicProperty;
import org.dynamic.datasource.util.IdUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author: JiangWH
 * @date: 2023/10/9 19:04
 * @version: 1.0.0
 */
public class DynamicDataSourceInterceptor implements MethodInterceptor {
    
    private final static Logger log = LoggerFactory.getLogger(DynamicDataSourceInterceptor.class);
    
    private final DynamicProperty dynamicProperty;
    
    public DynamicDataSourceInterceptor(DynamicProperty dynamicProperty) {
        this.dynamicProperty = dynamicProperty;
    }
    
    @Override
    public Object invoke(MethodInvocation invocation) throws Throwable {
        try {
            //  choose datasource
            String id = IdUtils.getId();
            DataSourceContextHolder.setId(id);
            return invocation.proceed();
        } catch (Exception e) {
            log.info("The data source does not exist.");
            throw new RuntimeException(e);
        } finally {
            DataSourceContextHolder.clearId();
        }
    }
    
}
