package com.plattensee.iocframework;

import com.plattensee.iocframework.container.DependencyContainer;
import com.plattensee.iocframework.container.IOCContainer;

import java.util.Objects;

public class SpiderFrameworkRunner {
    private static Class<? extends IOCContainer> containerType = DependencyContainer.class;

    public static synchronized <T extends IOCFrameworkRunner> void start(Class<T> entryPoint) {
        ApplicationContext context = new SpiderApplicationContext(containerType);
        IOCFrameworkRunner runner = context.initContext(entryPoint);
        runner.run();
    }

    public static synchronized <T extends IOCContainer> void setContainerType(Class<T> containerType) {
        containerType = Objects.requireNonNull(containerType, "Container type cannot be null");
    }
}
