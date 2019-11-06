package io.github.easyprofiler.model;

import java.util.List;


public class ControllerAccessInfo {
    private Class<?> clazz;
    private List<MethodAccessInfo> methods;

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public List<MethodAccessInfo> getMethods() {
        return methods;
    }

    public void setMethods(List<MethodAccessInfo> methods) {
        this.methods = methods;
    }
}
