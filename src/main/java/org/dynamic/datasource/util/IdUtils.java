package org.dynamic.datasource.util;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

/**
 * @author: JiangWH
 * @date: 2023/10/9 20:04
 * @version: 1.0.0
 */
public class IdUtils {

    public static String getId() {
        try {
            ClassLoader servletUtil = Class.forName("com.labscare.core.util.ServletUtil").getClassLoader();
            Class<?> jdkProxy = servletUtil.loadClass("com.labscare.core.util.ServletUtil");
            Object o = jdkProxy.newInstance();
            CglibService cglibProxyUserService = new CglibService(o);
            Object result = cglibProxyUserService.intercept(o, jdkProxy.getMethod("getCurrentUser", null), null, null);
    
            Object getCompanyId = result.getClass().getMethod("getCompanyId", null).invoke(result, null);
            return getCompanyId.toString();
        } catch (Throwable e) {
            e.printStackTrace();
        }
        return null;
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
