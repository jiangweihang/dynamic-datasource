package org.dynamic.datasource.annotation;

import org.dynamic.datasource.aop.ScheduleAop;
import org.dynamic.datasource.config.DynamicDataSourceConfig;
import org.dynamic.datasource.config.InterceptorConfig;
import org.dynamic.datasource.listener.MongoBeanListener;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author: JiangWH
 * @date: 2023/10/9 18:13
 * @version: 1.0.0
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Inherited
@Import({DynamicDataSourceConfig.class, MongoBeanListener.class, ScheduleAop.class, InterceptorConfig.class})
public @interface EnableDynamic {
    
    boolean value() default true;
    
}
