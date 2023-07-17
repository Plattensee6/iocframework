package com.plattensee.iocframework.container;

import com.plattensee.iocframework.annotation.Inject;
import com.plattensee.iocframework.exception.*;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class DependencyContainer implements IOCContainer {
    private Map<Class<?>, Object> registeredInstances = Collections.synchronizedMap(new HashMap<>());
    private List<Class<?>> classesInPath;

    @Override
    public void registerClassesInPath(List<Class<?>> classesInPath) {
        this.classesInPath = Objects.requireNonNull(classesInPath);
        classesInPath.forEach(this::register);
    }

    @Override
    public <T> T register(Class<T> newClass) {
        if (!registeredInstances.containsKey(Objects.requireNonNull(newClass))) {
            registeredInstances.put(newClass, resolve(newClass));
        }
        return newClass.cast(registeredInstances.get(newClass));
    }

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

    private <T> T createInstance(Class<T> clazz) throws NoSuchMethodException,
            InvocationTargetException, InstantiationException, IllegalAccessException {
        Constructor<?> constructor = clazz.getDeclaredConstructor();
        return clazz.cast(constructor.newInstance());
    }

    private Class<?> findImplementionClass(Class<?> parent, Field field) {
        String className = field.getAnnotation(Inject.class).className();
        return Objects.nonNull(className) && !className.isEmpty() ?
                findClassByName(className) : getAssignableClass(parent);
    }

    private Class<?> findClassByName(String className) {
        List<Class<?>> matchingClasses = Objects.requireNonNull(classesInPath, "First, you have to scan the packages.")
                .stream()
                .filter(clazz -> clazz.getName().toLowerCase().contains(className.toLowerCase()))
                .collect(Collectors.toList());
        return checkMatchingImplementationsQuantity(matchingClasses, className)
                .orElseThrow(ImplementationNotFoundException::new);
    }

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

    private Class<?> getAssignableClass(Class<?> type) {
        List<Class<?>> matchingClasses = Objects.requireNonNull(classesInPath, "First, you have to scan the packages.")
                .stream()
                .filter(type::isAssignableFrom).collect(Collectors.toList());

        return checkMatchingImplementationsQuantity(matchingClasses, type.getName())
                .orElseThrow(ImplementationNotFoundException::new);
    }
}
