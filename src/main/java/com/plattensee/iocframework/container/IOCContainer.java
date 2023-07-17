package com.plattensee.iocframework.container;

import java.util.List;

public interface IOCContainer {
    <T> T register(Class<T> newClass);

    <T> T resolve(Class<T> clazz);
    void registerClassesInPath(List<Class<?>> classesInPath);
}
