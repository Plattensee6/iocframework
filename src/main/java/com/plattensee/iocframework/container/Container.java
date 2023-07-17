package com.plattensee.iocframework.container;

import java.util.List;

public interface Container {
    <T> Object getInstance(Class<T> tClass);
    void registerClass(Class<?> newClass);
    void registerClasses(List<Class<?>> newClasses);
}
