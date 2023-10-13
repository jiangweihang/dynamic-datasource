package org.dynamic.datasource.config;

import org.dynamic.datasource.util.SpringContextUtils;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.stereotype.Component;

/**
 * @author: JiangWH
 * @date: 2023/10/11 16:31
 * @version: 1.0.0
 */
@Component
public class SpringContextInit implements ApplicationContextInitializer<ConfigurableApplicationContext> {
    
    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        SpringContextUtils.initContext(applicationContext);
    }
    
}
