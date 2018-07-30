package com.tzl.util;

import com.tzl.routing.RequestMapping;
import com.tzl.util.entity.HandlerEntity;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



public class ControllerRegistration {
    private ConcurrentMap<String,HandlerEntity> requestMapping = new ConcurrentHashMap<>();

    public ConcurrentMap<String, HandlerEntity> getRequestMapping() {
        return requestMapping;
    }
    /**
    * 初始化方法
    * */
    public void init() throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        String path = "com.tzl.controller";
        List<String> classPaths = ClassUtils.getClassName(path,true);
        isClass(classPaths);
    }

    /**
    * 注册控制器
    * */
    private void isClass(List<String> classList) throws ClassNotFoundException, IllegalAccessException, InstantiationException {
        for (String cls : classList) {
            Class c = Class.forName(cls);
            c.newInstance();
            Controller controller = (Controller) c.getAnnotation(Controller.class);
            if(controller != null){
                setRequestEntity(cls,c);
            }
        }
    }
    /**
    * 解析控制器方法
    * */
    private void setRequestEntity(String cls,Class c){
        String className = cls.substring(0,1).toLowerCase()+cls.substring(1);
        RequestMapping mapping = (RequestMapping) c.getAnnotation(RequestMapping.class);
        String classMapping = null;
        if(mapping != null){
            classMapping = mapping.value();
        }
        Method[] methods = c.getDeclaredMethods();
        for (Method m : methods) {
            RequestMapping methodMapping = m.getAnnotation(RequestMapping.class);
            if(methodMapping != null){
                HandlerEntity entity = new HandlerEntity();
                entity.setClassName(className);
                entity.setMethodName(m.getName());
                StringBuilder builder = new StringBuilder();
                if(classMapping != null && !classMapping.equals("")){
                    builder.append(classMapping);
                    builder.append("/");
                }
                builder.append(methodMapping.value());
                Class[] parameters = m.getParameterTypes();
                Map<String,Type> parametersMap = new LinkedHashMap<>();
                LocalVariableTableParameterNameDiscoverer parameterNames = new LocalVariableTableParameterNameDiscoverer();
                String[] names = parameterNames.getParameterNames(m);
                if(parameters != null){
                    for (int i=0; i<parameters.length; i++) {
                        parametersMap.put(names[i],parameters[i]);
                    }
                }
                entity.setParameters(parametersMap);
                requestMapping.put(builder.toString(),entity);
            }
        }
    }
}
