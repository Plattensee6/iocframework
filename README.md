# IOCFramework

IOCFramework is a lightweight Inversion of Control (IoC) framework for Java applications. This framework simplifies the management of dependencies and the configuration of your application.
## Features

1. #### Dependency Management
IOCFramework simplifies the management of dependencies in your Java application. It allows you to define and inject dependencies with ease, reducing code complexity.

2. #### Annotation-Based Configuration

Configure your application using annotations, such as @Inject, @Component, and @Autowired, to define and resolve dependencies automatically.

3. ### Package Scanning
Scans packages and their sub-packages to discover and register classes. This feature is particularly useful for locating and managing components in your application.

4. ### Flexible Container Types
Choose from various container types to suit your application's needs. The framework provides a default DependencyContainer, but you can create custom container implementations as well.
## Getting Started


To start using IOCFramework, follow these steps:

1. Include the framework in your project's dependencies.
    
2. Define an entry point class that implements the IOCFrameworkRunner interface:
```java
public class MyApplication implements IOCFrameworkRunner {
    @Override
    public void run() {
        // Your application logic here
    }
```

 3. Use annotations to define components:
```java
@Component
public class MyService {
    // Your service methods here
}
```
 4. Inject dependencies:
```java
@Component
public class MyService {
    @Inject
    private AnotherService anotherService;
}
```

 5. Initialize the framework using the SpiderFrameworkRunner:
```java
@Component
public static void main(String[] args) {
    SpiderFrameworkRunner.start(MyApplication.class);
}
```
### Usage Tips
 Always provide a no-arg constructor for your classes to ensure proper initialization.
## License
This project is licensed under the MIT License.
