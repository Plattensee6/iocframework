package com.plattensee.iocframework.container;

import com.plattensee.iocframework.annotation.Inject;
import com.plattensee.iocframework.exception.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The DependencyContainer class is an implementation of the IOCContainer interface that manages class dependencies and
 * provides dependency resolution functionality.
 */
public class DependencyContainer implements IOCContainer {
    private Map<Class<?>, Object> registeredInstances = Collections.synchronizedMap(new HashMap<>());
    private List<Class<?>> classesInPath;

    /**
     * Registers the classes in the specified path within the container.
     *
     * @param classesInPath a list of classes to register in the container
     */
    @Override
    public void registerClassesInPath(List<Class<?>> classesInPath) {
        this.classesInPath = Objects.requireNonNull(classesInPath);
        classesInPath.forEach(this::register);
    }

    /**
     * Registers the specified class in the container.
     *
     * @param <T>      the type of the class to register
     * @param newClass the class object representing the new class to register
     * @return the registered instance of the specified class
     */
    @Override
    public <T> T register(Class<T> newClass) {
        if (!registeredInstances.containsKey(Objects.requireNonNull(newClass))) {
            registeredInstances.put(newClass, resolve(newClass));
        }
        return newClass.cast(registeredInstances.get(newClass));
    }

    /**
     * Resolves the dependencies for the specified class and returns an instance of it.
     *
     * @param <T>   the type of the class to resolve
     * @param clazz the class object representing the class to resolve
     * @return an instance of the specified class with resolved dependencies
     * @throws WiredConstructorNotFoundException   if a no-arg constructor is not found for the class
     * @throws WiredConstructorInvocationException if there is an error invoking the constructor
     * @throws WiredConstructorNotAccessible       if the constructor is not accessible
     */
    @Override
    public <T> T resolve(Class<T> clazz) {
        try {
            T instance = createInstance(clazz);
            injectDependencies(instance);
            return instance;
        } catch (NoSuchMethodException exception) {
            throw new WiredConstructorNotFoundException("Sorry bro, I need a no-arg constructor!", exception);
        } catch (InvocationTargetException | InstantiationException exception) {
            throw new WiredConstructorInvocationException(exception);
        } catch (IllegalAccessException exception) {
            throw new WiredConstructorNotAccessible(exception);
        }
    }

    /**
     * Injects dependencies into the specified object instance by using reflection.
     *
     * @param instance the object instance to inject dependencies into
     * @throws IllegalAccessException if there is an error accessing a field during dependency injection
     */
    private void injectDependencies(Object instance) throws IllegalAccessException {
        Class<?> clazz = instance.getClass();
        Field[] fields = clazz.getDeclaredFields();
        for (Field field : fields) {
            if (field.isAnnotationPresent(Inject.class)) {
                Class<?> fieldType = field.getType();
                if (fieldType.isInterface() || Modifier.isAbstract(fieldType.getModifiers())) {
                    fieldType = findImplementionClass(fieldType, field);
                }
                Object dependencyInstance = resolve(fieldType);
                field.setAccessible(true);
                field.set(instance, dependencyInstance);
            }
        }
    }

    /**
     * Creates an instance of the specified class using reflection.
     *
     * @param <T>   the type of the class to create an instance of
     * @param clazz the class object representing the class to create an instance of
     * @return an instance of the specified class
     * @throws NoSuchMethodException     if the no-arg constructor for the class is not found
     * @throws InvocationTargetException if there is an error invoking the constructor
     * @throws InstantiationException    if there is an error instantiating the class
     * @throws IllegalAccessException    if there is an error accessing the class or constructor
     */
    private <T> T createInstance(Class<T> clazz) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return clazz.cast(constructor.newInstance());
    }

    /**
     * Finds the implementation class for a given parent class or interface based on the @Inject annotation.
     *
     * @param parent the parent class or interface
     * @param field  the field containing the @Inject annotation
     * @return the implementation class found based on the annotation or assignable class
     */
    private Class<?> findImplementionClass(Class<?> parent, Field field) {
        String className = field.getAnnotation(Inject.class).className();
        return Objects.nonNull(className) && !className.isEmpty() ?
                findClassByName(className) : getAssignableClass(parent);
    }

    /**
     * Finds a class by its name within the list of classes registered in the container.
     *
     * @param className the name of the class to find
     * @return the class object representing the found class
     * @throws ImplementationNotFoundException if the class implementation is not found
     */
    private Class<?> findClassByName(String className) {
        List<Class<?>> matchingClasses = Objects.requireNonNull(classesInPath, "First, you have to scan the packages.")
                .stream()
                .filter(clazz -> clazz.getName().toLowerCase().contains(className.toLowerCase()))
                .collect(Collectors.toList());
        return checkMatchingImplementationsQuantity(matchingClasses, className)
                .orElseThrow(ImplementationNotFoundException::new);
    }

    /**
     * Checks the quantity of matching implementations for a given class.
     *
     * @param matchingImplementations the list of matching implementation classes
     * @param className               the name of the class being checked
     * @return an optional containing the found class if it exists
     * @throws UndefinedImplementationException if multiple implementations are found for the class
     * @throws ImplementationNotFoundException  if no implementation is found for the class
     */
    private Optional<Class<?>> checkMatchingImplementationsQuantity(List<Class<?>> matchingImplementations, String className) {
        if (matchingImplementations.size() > 1) {
            throw new UndefinedImplementationException(
                    String.format("There are multiple implementations for %s interface or abstract class. " +
                            "Use @Inject(className=exampleClass) annotation to specify the implementing class.", className));
        }
        if (matchingImplementations.isEmpty()) {
            throw new ImplementationNotFoundException(
                    String.format("Implementation not found for %s interface or abstract class. " +
                            "Use @Inject(className=exampleClass) annotation to specify the implementing class.", className));
        }
        return Optional.ofNullable(matchingImplementations.get(0));
    }

    /**
     * Gets an assignable class based on a given type from the list of classes registered in the container.
     *
     * @param type the type of the class to find
     * @return the class object representing the found assignable class
     * @throws ImplementationNotFoundException if no assignable class is found
     */
    private Class<?> getAssignableClass(Class<?> type) {
        List<Class<?>> matchingClasses = Objects.requireNonNull(classesInPath, "First, you have to scan the packages.")
                .stream()
                .filter(type::isAssignableFrom).collect(Collectors.toList());

        return checkMatchingImplementationsQuantity(matchingClasses, type.getName())
                .orElseThrow(ImplementationNotFoundException::new);
    }
}
