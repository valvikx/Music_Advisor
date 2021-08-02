package by.valvik.musicadvisor.context;

import by.valvik.musicadvisor.annotation.Configuration;
import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.context.config.Config;
import by.valvik.musicadvisor.exception.ConfigurationException;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

import static java.beans.Introspector.decapitalize;
import static java.util.stream.Collectors.toMap;

public class ApplicationContext {

    private final Map<String, Object> cache;

    private ObjectFactory objectFactory;

    private final Config config;

    public ApplicationContext(Config config) {

        this.config = config;

        this.cache = new HashMap<>();

    }

    public void initSingletonObjects() {

        initSingletonFromConfigurationClass();

        initSingletonFromAnnotatedClass();

    }

    public <T> T getObject(Class<T> tClass) {

        String tClassName = decapitalize(tClass.getSimpleName());

        if (cache.containsKey(tClassName)) {

            return (T) cache.get(tClassName);

        }

        return createObject(tClassName, tClass);

    }

    public <T> Optional<T> getObject(String qualifier) {

        return Optional.ofNullable((T) cache.get(qualifier));

    }

    public void addObjects(Map<String, Object> objects) {

        cache.putAll(objects);

    }

    public void addObject(Object object) {

        String objectName = decapitalize(object.getClass().getSimpleName());

        cache.put(objectName, object);

    }

    public Config getConfig() {

        return config;

    }

    public void setObjectFactory(ObjectFactory objectFactory) {

        this.objectFactory = objectFactory;

    }

    private void initSingletonFromAnnotatedClass() {

        Set<Class<?>> singletonClasses = config.getAnnotatedClasses(Singleton.class);

        singletonClasses.forEach(this::getObject);

    }

    private void initSingletonFromConfigurationClass() {

        Set<Class<?>> configurationClasses = config.getAnnotatedClasses(Configuration.class);

        if (!configurationClasses.isEmpty()) {

            Map<String, Object> objects = configurationClasses.stream()
                    .flatMap(c -> Arrays.stream(c.getDeclaredMethods())
                            .filter(m -> m.isAnnotationPresent(Singleton.class)))
                    .collect(toMap(Method::getName, m -> {

                        Object createdObject = objectFactory.create(m.getDeclaringClass());

                        try {

                            return m.invoke(createdObject);

                        } catch (IllegalAccessException | InvocationTargetException e) {

                            throw new ConfigurationException(e);

                        }

                    }));

            addObjects(objects);

        }
    }

    private <T> T createObject(String qualifier, Class<T> tClass) {

        T t = objectFactory.create(tClass);

        if (tClass.isAnnotationPresent(Singleton.class)) {

            cache.put(qualifier, t);

        }

        return t;
    }

}