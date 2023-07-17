package com.plattensee.iocframework.container;

public class ContainerFactory {
    private static final ContainerFactory factoryInstance = new ContainerFactory();

    private ContainerFactory() {
    }

    public <T extends IOCContainer> T createInstance(Class<T> containerType) {
        try {
            return containerType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("Unable to instantiate %s", containerType.getName()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Unable to access no-arg constructor in %s", containerType.getName()), e);
        }
    }
    public static synchronized ContainerFactory getFactoryInstance(){
        return factoryInstance;
    }
}
