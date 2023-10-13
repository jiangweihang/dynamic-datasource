package org.dynamic.datasource.aop;

import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.dynamic.datasource.core.DataSourceContextHolder;
import org.dynamic.datasource.model.DynamicSourceInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.List;

/**
 * 拦截定时任务处理
 * @author: JiangWH
 * @date: 2023/10/10 10:51
 * @version: 1.0.0
 */
@Aspect
@Component
public class ScheduleAop {
    
    private final static Logger log = LoggerFactory.getLogger(ScheduleAop.class);
    
    static {
        log.info("定时任务AOP加载完毕.");
    }

    @Pointcut("@annotation(org.springframework.scheduling.annotation.Scheduled)")
    private void scheduledPointcut() {}
    
    @Around("scheduledPointcut()")
    public Object annotationAround(ProceedingJoinPoint joinPoint) throws Throwable {
        try {
            List<String> dataSourceIds = DynamicSourceInfo.getDataSourceIds();
            if(CollectionUtils.isEmpty(dataSourceIds)) {
                return joinPoint.proceed();
            } else {
                for (String dataSourceId : dataSourceIds) {
                    DataSourceContextHolder.setId(dataSourceId);
                    joinPoint.proceed();
                    DataSourceContextHolder.clearId();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            DataSourceContextHolder.clearId();
        }
        return new Object();
    }

}
