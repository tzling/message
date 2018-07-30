package com.tzl.util.entity;

import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by tzl on 2018/7/2.
 */
public class HandlerEntity {
    private String methodName;
    private String className;
    private Map<String,Type> parameters;

    public String getMethodName() {
        return methodName;
    }

    public void setMethodName(String methodName) {
        this.methodName = methodName;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Map<String, Type> getParameters() {
        return parameters;
    }

    public void setParameters(Map<String, Type> parameters) {
        this.parameters = parameters;
    }
}
