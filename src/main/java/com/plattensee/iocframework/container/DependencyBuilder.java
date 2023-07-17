package com.plattensee.iocframework.container;

public interface DependencyBuilder {
    void loadDependencies(IOCContainer container, String packageName);
}
