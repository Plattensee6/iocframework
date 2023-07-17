package com.plattensee.iocframework;

public interface ApplicationContext {
    /**
     * Initializes the application context with the specified entry point class.
     *
     * @param <T>        the type of the IOCFrameworkRunner
     * @param entryPoint the class object representing the entry point of the IOC framework
     * @return an instance of the specified IOCFrameworkRunner type
     */
    <T extends IOCFrameworkRunner> T initContext(Class<T> entryPoint);
}
