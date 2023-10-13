package org.dynamic.datasource.listener;

import org.springframework.beans.factory.support.DefaultListableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.SpringApplicationRunListener;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;

/**
 * @author: JiangWH
 * @date: 2023/10/11 17:18
 * @version: 1.0.0
 */
public class MongoInitListener implements SpringApplicationRunListener {
    
    private final SpringApplication application;
    private final String[] args;
    
    public MongoInitListener(SpringApplication sa, String[] args) {
        this.application = sa;
        this.args = args;
    }
    
    @Override
    public void starting() { }
    
    @Override
    public void environmentPrepared(ConfigurableEnvironment environment) { }
    
    @Override
    public void contextPrepared(ConfigurableApplicationContext context) { }
    
    @Override
    public void contextLoaded(ConfigurableApplicationContext context) {
    
    }
    
    @Override
    public void finished(ConfigurableApplicationContext context, Throwable exception) {
        /*
        目前有两种思路:
            1、代理已经注册的 MongoTemplate. (怎么代理, 代理完后如何使用)
            2、销毁已经注册的 MongoTemplate, 重新注册一个新的MongoTemplate. (如果已经有地方在使用注册好的MongoTemplate, 这个方案就不太好)
         */
        DefaultListableBeanFactory beanFactory = (DefaultListableBeanFactory)context.getBeanFactory();
        beanFactory.destroySingleton("globalMongoTemplate");
    }
    
}
