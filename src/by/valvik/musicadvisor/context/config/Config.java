package by.valvik.musicadvisor.context.config;

import by.valvik.musicadvisor.context.configurator.ObjectConfigurator;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface Config {

    <T> Optional<Class<? extends T>> getSingleImplClass(Class<T> infClass);

    <T> Optional<Class<? extends T>> getImplClassByQualifier(Class<T> infClass, String qualifier);

    List<? extends ObjectConfigurator> getConfiguratorObjects();

    Set<Class<?>> getAnnotatedClasses(Class<? extends Annotation> annClass);

}
