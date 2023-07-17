package com.plattensee.iocframework;

import com.plattensee.iocframework.container.ContainerFactory;
import com.plattensee.iocframework.container.IOCContainer;
import com.plattensee.iocframework.locator.ClassLocator;
import com.plattensee.iocframework.locator.PackageClassScanner;

public class SpiderApplicationContext implements ApplicationContext {
    private final IOCContainer iocContainer;

    public SpiderApplicationContext(Class<? extends IOCContainer> containerType) {
        this.iocContainer = ContainerFactory.getFactoryInstance().createInstance(containerType);
    }

    @Override
    public <T extends IOCFrameworkRunner> T initContext(Class<T> entryPoint) {
        ClassLocator classLocator = PackageClassScanner.getInstance();
        iocContainer.registerClassesInPath(classLocator.scan(entryPoint.getPackage().getName()));
        return iocContainer.register(entryPoint);
    }
}
