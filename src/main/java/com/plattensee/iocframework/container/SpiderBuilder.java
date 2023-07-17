package com.plattensee.iocframework.container;

import com.plattensee.iocframework.locator.ClassLocator;

import java.util.List;

public class SpiderBuilder implements DependencyBuilder {
    private final ClassLocator classLocator;

    public SpiderBuilder(ClassLocator classLocator) {
        this.classLocator = classLocator;
    }

    @Override
    public void loadDependencies(IOCContainer container, String packageName) {
        List<Class<?>> classes = classLocator.scan(packageName);
        classes.forEach(container::register);
    }
}
