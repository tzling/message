package com.tzl.controller;

import com.tzl.controller.Entity.HandlerEntity;
import com.tzl.routing.RequestMapping;
import com.tzl.util.ClassUtils;
import org.springframework.core.LocalVariableTableParameterNameDiscoverer;
import org.springframework.stereotype.Controller;

import java.io.IOException;
import java.lang.reflect.Method;
import java.lang.reflect.Type;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;



public class ControllerHandler {
    private static final ConcurrentMap<String,HandlerEntity> requestMapping = new ConcurrentHashMap<>();

    public void init() throws IllegalAccessException, InstantiationException, ClassNotFoundException, IOException {
        String path = "com.tzl.controller";
        List<String> classPaths = ClassUtils.getClassName(path,true);
        isClass(classPaths);
    }

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
    public static void main(String[] args) throws ClassNotFoundException, IOException, InstantiationException, IllegalAccessException {
        ControllerHandler handler = new ControllerHandler();
        handler.init();
        System.out.println(handler.requestMapping);
    }
}
