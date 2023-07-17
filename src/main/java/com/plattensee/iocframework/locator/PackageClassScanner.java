package com.plattensee.iocframework.locator;

import com.plattensee.iocframework.annotation.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * The PackageClassScanner is an implementation of the ClassLocator interface that scans a package and its sub-packages
 * for classes annotated with the @Component annotation.
 */
public class PackageClassScanner implements ClassLocator {
    private static volatile PackageClassScanner instance = new PackageClassScanner();

    private PackageClassScanner() {
    }

    /**
     * Scans the specified package and its sub-packages for classes annotated with the @Component annotation.
     *
     * @param packageName the name of the package to scan
     * @return a list of classes found in the package and its sub-packages
     * @throws NullPointerException if the package name is null
     */
    @Override
    public List<Class<?>> scan(String packageName) {
        List<Class<?>> classes = new ArrayList<>();

        String packagePath = Objects.requireNonNull(packageName, "Package cannot be null!")
                .replaceAll("\\.", "\\" + File.separator); // Escape the file separator
        String classPath = System.getProperty("java.class.path");
        String[] classPathEntries = classPath.split(File.pathSeparator);

        for (String classPathEntry : classPathEntries) {
            File baseDir = new File(classPathEntry);
            String packageDirPath = baseDir.getAbsolutePath().concat(File.separator).concat(packagePath);
            scanPackageRecursive(packageName, packageDirPath, classes);
        }
        return classes;
    }

    /**
     * Recursively scans a package and its sub-packages for classes annotated with @Component and adds them to the provided list.
     *
     * @param packageName    the name of the current package being scanned
     * @param packageDirPath the absolute path of the current package directory
     * @param classes        the list of classes to add the found classes to
     */
    private void scanPackageRecursive(String packageName, String packageDirPath, List<Class<?>> classes) {
        File packageDir = new File(packageDirPath);

        if (packageDir.exists() && packageDir.isDirectory()) {
            File[] files = packageDir.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file.isFile() && file.getName().endsWith(".class")) {
                        String className = packageName + '.' + file.getName().substring(0, file.getName().length() - 6);
                        try {
                            Class<?> clazz = Class.forName(className);
                            if (clazz.isAnnotationPresent(Component.class)) {
                                classes.add(clazz);
                            }
                        } catch (ClassNotFoundException e) {
                            e.printStackTrace();
                        }
                    } else if (file.isDirectory()) {
                        String subPackageName = packageName + '.' + file.getName();
                        String subPackageDirPath = packageDirPath + File.separator + file.getName();
                        scanPackageRecursive(subPackageName, subPackageDirPath, classes);
                    }
                }
            }
        }
    }

    public static synchronized PackageClassScanner getInstance() {
        return instance;
    }
}