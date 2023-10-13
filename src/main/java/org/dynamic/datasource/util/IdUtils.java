package org.dynamic.datasource.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

/**
 * @author: JiangWH
 * @date: 2023/10/9 20:04
 * @version: 1.0.0
 */
@Component
public class IdUtils {
    
    private static String queryIdClass;
    
    private static String method;

    public static String getId() {
        try {
            ClassLoader servletUtil = Class.forName(queryIdClass).getClassLoader();
            Class<?> jdkProxy = servletUtil.loadClass(queryIdClass);
            Object o = jdkProxy.newInstance();
            CglibService cglibProxyUserService = new CglibService(o);
            Object result = cglibProxyUserService.intercept(o, jdkProxy.getMethod(method, null), null, null);
            if(result == null) {
                throw new NullPointerException("Unable to obtain id");
            }
            return result.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
    }
    
    @Value("${custom.dynamic.idInfo.queryIdClass}")
    public void setQueryIdClass(String queryIdClass) {
        IdUtils.queryIdClass = queryIdClass;
    }
    
    @Value("${custom.dynamic.idInfo.method}")
    public void setMethod(String method) {
        IdUtils.method = method;
    }
}

class CglibService implements MethodInterceptor {
    
    private Object object;
    
    public CglibService(Object object) {
        this.object = object;
    }
    
    public Object getProxyInstance() {
        Enhancer enhancer = new Enhancer();
        enhancer.setSuperclass(object.getClass());
        enhancer.setCallback(this);
        return enhancer.create();
    }
    
    @Override
    public Object intercept(Object o, Method method, Object[] objects, MethodProxy methodProxy) throws Throwable {
        return method.invoke(object, objects);
    }
    
}
