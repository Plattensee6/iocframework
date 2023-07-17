package com.plattensee.iocframework.container;

/**
 * The ContainerFactory class provides a factory method for creating instances of IOCContainer implementations.
 * It follows the singleton pattern, allowing only one instance of the factory to exist.
 */
public class ContainerFactory {
    private static final ContainerFactory factoryInstance = new ContainerFactory();

    private ContainerFactory() {
    }

    /**
     * Creates and returns an instance of the specified IOCContainer type.
     *
     * @param <T>           the type of the IOCContainer
     * @param containerType the class object representing the IOCContainer type
     * @return an instance of the specified IOCContainer type
     * @throws RuntimeException if there is an error instantiating the IOCContainer
     */
    public <T extends IOCContainer> T createInstance(Class<T> containerType) {
        try {
            return containerType.newInstance();
        } catch (InstantiationException e) {
            throw new RuntimeException(String.format("Unable to instantiate %s", containerType.getName()), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(String.format("Unable to access no-arg constructor in %s", containerType.getName()), e);
        }
    }

    /**
     * Returns the singleton instance of the ContainerFactory.
     * @return the singleton instance of the ContainerFactory
     */
    public static synchronized ContainerFactory getFactoryInstance() {
        return factoryInstance;
    }
}
