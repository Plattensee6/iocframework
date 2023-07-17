package com.plattensee.iocframework.locator;

import com.plattensee.iocframework.annotation.Component;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class PackageClassScanner implements ClassLocator {
    private static volatile PackageClassScanner instance = new PackageClassScanner();
    private PackageClassScanner() {
    }

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