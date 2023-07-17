package com.plattensee.iocframework;

import com.plattensee.iocframework.container.ContainerFactory;
import com.plattensee.iocframework.container.IOCContainer;
import com.plattensee.iocframework.locator.ClassLocator;
import com.plattensee.iocframework.locator.PackageClassScanner;

/**
 * The SpiderApplicationContext class is an implementation of the ApplicationContext interface specifically designed for the Spider IOC framework.
 * It uses a specified IOCContainer implementation to manage dependencies and initializes the context using an entry point class.
 */
public class SpiderApplicationContext implements ApplicationContext {
    private final IOCContainer iocContainer;

    /**
     * Constructs a new SpiderApplicationContext with the specified container type.
     *
     * @param containerType the class object representing the container type to use
     */
    public SpiderApplicationContext(Class<? extends IOCContainer> containerType) {
        this.iocContainer = ContainerFactory.getFactoryInstance().createInstance(containerType);
    }

    /**
     * Initializes the Spider application context with the specified entry point class.
     * Scans for classes in the same package as the entry point and registers them in the container.
     * Finally, registers and returns an instance of the specified entry point class.
     *
     * @param <T>        the type of the IOCFrameworkRunner
     * @param entryPoint the class object representing the entry point of the IOC framework
     * @return an instance of the specified IOCFrameworkRunner type
     */
    @Override
    public <T extends IOCFrameworkRunner> T initContext(Class<T> entryPoint) {
        ClassLocator classLocator = PackageClassScanner.getInstance();
        iocContainer.registerClassesInPath(classLocator.scan(entryPoint.getPackage().getName()));
        return iocContainer.register(entryPoint);
    }
}
