package com.plattensee.iocframework.locator;

import java.util.List;

public interface ClassLocator {
    List<Class<?>> scan(String packageName);
}
