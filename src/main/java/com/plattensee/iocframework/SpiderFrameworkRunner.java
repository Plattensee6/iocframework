package com.plattensee.iocframework;

import com.plattensee.iocframework.container.DependencyContainer;
import com.plattensee.iocframework.container.IOCContainer;

import java.util.Objects;

/**
 * The SpiderFrameworkRunner class is a runner class for the Spider IOC framework. It provides methods to start the framework
 * and set the container type.
 */
public class SpiderFrameworkRunner {
    private static Class<? extends IOCContainer> containerType = DependencyContainer.class;

    /**
     * Starts the Spider IOC framework with the specified entry point class.
     *
     * @param <T>        the type of the IOCFrameworkRunner
     * @param entryPoint the class object representing the entry point of the IOC framework
     */
    public static synchronized <T extends IOCFrameworkRunner> void start(Class<T> entryPoint) {
        ApplicationContext context = new SpiderApplicationContext(containerType);
        IOCFrameworkRunner runner = context.initContext(entryPoint);
        runner.run();
    }
    /**

     Sets the container type for the IOC framework.
     @param <T> the type of the IOCContainer
     */
    public static synchronized <T extends IOCContainer> void setContainerType(Class<T> containerType) {
        containerType = Objects.requireNonNull(containerType, "Container type cannot be null");
    }
}
