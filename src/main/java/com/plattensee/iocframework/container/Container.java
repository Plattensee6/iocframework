package com.plattensee.iocframework.container;

import java.util.List;

public interface Container {
    /**
     * Retrieves an instance of the specified class from the container.
     *
     * @param <T>    the type of the class
     * @param tClass the class object representing the type T
     * @return an instance of the specified class, or null if not found
     */
    <T> Object getInstance(Class<T> tClass);

    /**
     * Registers a new class in the container.
     *
     * @param newClass the class object representing the new class to register
     */
    void registerClass(Class<?> newClass);

    /**
     * Registers multiple classes in the container.
     *
     * @param newClasses a list of class objects representing the new classes to register
     */
    void registerClasses(List<Class<?>> newClasses);
}
