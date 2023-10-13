package org.dynamic.datasource.config;

import org.dynamic.datasource.filter.DynamicDataSourceInterceptor;
import org.dynamic.datasource.model.DynamicProperty;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.aspectj.AspectJExpressionPointcutAdvisor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @author: JiangWH
 * @date: 2023/10/9 19:07
 * @version: 1.0.0
 */
@Configuration
@ConditionalOnClass(DynamicDataSourceInterceptor.class)
public class InterceptorConfig {
    
    private final static Logger log = LoggerFactory.getLogger(InterceptorConfig.class);
    
    @Autowired
    private DynamicProperty dynamicProperty;

    @Bean
    public AspectJExpressionPointcutAdvisor logAdvisor() {
        AspectJExpressionPointcutAdvisor advisor = new AspectJExpressionPointcutAdvisor();
        advisor.setExpression(dynamicProperty.getAspect().getExpression());
        advisor.setAdvice(new DynamicDataSourceInterceptor(dynamicProperty));
        log.info("切换数据源AOP加载完毕.");
        return advisor;
    }

}
