package org.dynamic.datasource.config;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.dynamic.datasource.model.DataBaseInfo;
import org.dynamic.datasource.model.DynamicProperty;
import org.dynamic.datasource.model.DynamicSourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.jdbc.DataSourceProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 暂时废弃不用
 * @author: JiangWH
 * @date: 2023/10/9 18:13
 * @version: 1.0.0
 */
//@Component
public class DynamicDataSourceConfig {
    
    private final static Logger log = LoggerFactory.getLogger(DynamicDataSourceConfig.class);
    
    @Lazy
    @Autowired
    private DataSource druidDataSource;
    
    @Autowired
    private DynamicProperty dynamicProperty;
    
    @Bean
    public DataSource defaultDataSource(DataSourceProperties properties) {
        HikariConfig hikariConfig = new HikariConfig();
        BeanUtils.copyProperties(properties, hikariConfig);
        hikariConfig.setJdbcUrl(properties.getUrl());
        return new HikariDataSource(hikariConfig);
    }
    
    @Bean
    @Primary
    public DataSource customDataSource() {
        return customDataSource(druidDataSource);
    }
    
    public DataSource customDataSource(DataSource defaultDataSource) {
        List<DataBaseInfo> dataBaseInfos = dataBaseInfoList(defaultDataSource);
        
        Map<Object, Object> targetDataSources = new HashMap<>();
        if (dataBaseInfos.size() > 0) {
            for (DataBaseInfo info : dataBaseInfos) {
                HikariConfig hikariConfig = new HikariConfig();
                BeanUtils.copyProperties(info, hikariConfig);
                hikariConfig.setJdbcUrl(info.getUrl());
                targetDataSources.put(info.getId(), new HikariDataSource(hikariConfig));
                DynamicSourceInfo.add(info.getId());
                log.info("租户[{}}数据库配置初始化完毕", info.getId());
            }
        }
        return new DynamicDataSource(defaultDataSource, targetDataSources);
    }
    
    private List<DataBaseInfo> dataBaseInfoList(DataSource dataSource) {
        try {
            List<DataBaseInfo> result = new ArrayList<DataBaseInfo>();
            Connection conn = dataSource.getConnection();
            
            PreparedStatement ps = conn.prepareStatement(dynamicProperty.getTableInfo().getSql());
            ResultSet rs = ps.executeQuery();
            while (rs.next()) {
                String driver = rs.getString(dynamicProperty.getTableInfo().getDriverClassNameColumn());
                if(StringUtils.isEmpty(driver)) {
                    continue;
                }
    
                DataBaseInfo dbSource = new DataBaseInfo();
                dbSource.setId(rs.getString(dynamicProperty.getTableInfo().getId()));
                dbSource.setDriverClassName(driver);
                dbSource.setUrl(rs.getString(dynamicProperty.getTableInfo().getUrlColumn()));
                dbSource.setUsername(rs.getString(dynamicProperty.getTableInfo().getUsernameColumn()));
                dbSource.setPassword(rs.getString(dynamicProperty.getTableInfo().getPasswordColumn()));
                result.add(dbSource);
            }
            rs.close();
            ps.close();
            conn.close();
            return result;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        
    }
    
}
