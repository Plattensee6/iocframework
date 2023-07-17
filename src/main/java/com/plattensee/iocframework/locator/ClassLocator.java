package com.plattensee.iocframework.locator;

import java.util.List;

/**
 * The ClassLocator interface defines the contract for a class that can locate classes in a package.
 */
public interface ClassLocator {
    /**
     * Scans the specified package and returns a list of classes found.
     *
     * @param packageName the name of the package to scan
     * @return a list of classes found in the package
     */
    List<Class<?>> scan(String packageName);
}
