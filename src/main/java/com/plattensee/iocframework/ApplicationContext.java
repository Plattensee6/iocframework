package com.plattensee.iocframework;

public interface ApplicationContext {
    <T extends IOCFrameworkRunner> T initContext(Class<T> entryPoint);
}
