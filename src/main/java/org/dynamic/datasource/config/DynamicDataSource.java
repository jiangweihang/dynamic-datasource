package org.dynamic.datasource.config;

import org.dynamic.datasource.core.DataSourceContextHolder;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;

import javax.sql.DataSource;
import java.util.Map;

/**
 * 重写获取数据源方式
 * @author: JiangWH
 * @date: 2023/10/9 18:37
 * @version: 1.0.0
 */
public class DynamicDataSource extends AbstractRoutingDataSource {
    
    public DynamicDataSource(DataSource defaultDataSource, Map<Object, Object> dataSources) {
        super.setDefaultTargetDataSource(defaultDataSource);
        super.setTargetDataSources(dataSources);
        super.afterPropertiesSet();
    }
    
    @Override
    protected Object determineCurrentLookupKey() {
        return DataSourceContextHolder.getId();
    }
    
}
