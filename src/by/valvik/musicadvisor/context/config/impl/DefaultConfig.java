package by.valvik.musicadvisor.context.config.impl;

import by.valvik.musicadvisor.annotation.Singleton;
import by.valvik.musicadvisor.context.config.Config;
import by.valvik.musicadvisor.context.configurator.ObjectConfigurator;
import by.valvik.musicadvisor.exception.ConfigurationException;
import org.reflections.Reflections;

import java.lang.annotation.Annotation;
import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static java.util.Optional.empty;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toSet;

public class DefaultConfig implements Config {

    private final Reflections reflections;

    public DefaultConfig(String packagesPrefix) {

        this.reflections = new Reflections(packagesPrefix);

    }

    @Override
    public <T> Optional<Class<? extends T>> getSingleImplClass(Class<T> infClass) {

        Set<Class<? extends T>> implClasses = getSubTypesOf(infClass);

        if (implClasses.size() != 1) {

           return empty();

        }

        return of(implClasses.iterator().next());
        
    }

    @Override
    public <T> Optional<Class<? extends T>> getImplClassByQualifier(Class<T> infClass, String qualifier) {

        Set<Class<? extends T>> classes = getSubTypesOf(infClass);

        return classes.stream()
                      .filter(c -> c.getDeclaredAnnotation(Singleton.class).qualifier().equals(qualifier))
                      .findFirst();

    }

    @Override
    public List<? extends ObjectConfigurator> getConfiguratorObjects() {

        Set<Class<? extends ObjectConfigurator>> classes = reflections.getSubTypesOf(ObjectConfigurator.class);

        return classes.stream()
                      .map(c -> {

                          try {

                              return c.getDeclaredConstructor().newInstance();

                          } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

                              throw new ConfigurationException(e);

                          }
                      })
                      .toList();

    }

    @Override
    public Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annClass) {

        Set<Class<?>> annotatedTypes = reflections.getTypesAnnotatedWith(annClass);

        Set<Class<?>> annotatedInterfaces = annotatedTypes.stream()
                                                          .filter(Class::isInterface)
                                                          .collect(toSet());

        annotatedTypes.removeAll(annotatedInterfaces);

        return annotatedTypes;

    }

    private <T> Set<Class<? extends T>> getSubTypesOf(Class<T> infClass) {

        return reflections.getSubTypesOf(infClass);

    }

}
