package by.valvik.musicadvisor.context;

import by.valvik.musicadvisor.annotation.Inject;
import by.valvik.musicadvisor.context.configurator.ObjectConfigurator;
import by.valvik.musicadvisor.exception.ConfigurationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;

public class ObjectFactory {

    private final ApplicationContext context;

    private final List<? extends ObjectConfigurator> configurators;

    public ObjectFactory(ApplicationContext context) {

        this.context = context;

        this.configurators = context.getConfig().getConfiguratorObjects();

    }

    public <T> T create(Class<T> tClass) {

        try {

            boolean hasAnnotatedCtor = Arrays.stream(tClass.getDeclaredConstructors())
                                             .anyMatch(c -> c.isAnnotationPresent(Inject.class));

            T t = hasAnnotatedCtor ? createWhenAnnotatedCtor(tClass) : createWithNoAnnotation(tClass);

            configurators.forEach(oc -> oc.configure(t, context));

            return t;

        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {

            throw new ConfigurationException(e);

        }

    }

    private <T> T createWhenAnnotatedCtor(Class<T> tClass) throws InstantiationException,
                                                                  IllegalAccessException,
                                                                  InvocationTargetException,
                                                                  NoSuchMethodException {

        Constructor<?> constructor = Arrays.stream(tClass.getDeclaredConstructors())
                                           .filter(c -> c.isAnnotationPresent(Inject.class))
                                           .findFirst()
                                           .get();

        Parameter[] parameters = constructor.getParameters();

        if (parameters.length == 0) {

            return (T) constructor.newInstance();

        }

        Object[] objects = getParameterObjects(parameters);

        return (T) constructor.newInstance(objects);

    }

    private <T> T createWithNoAnnotation(Class<T> tClass) throws InstantiationException,
                                                                 IllegalAccessException,
                                                                 InvocationTargetException,
                                                                 NoSuchMethodException {

        return tClass.getDeclaredConstructor().newInstance();

    }

    private Object[] getParameterObjects(Parameter[] parameters) {

        return Arrays.stream(parameters)
                     .map(p -> {

                             Class<?> parameterType = p.getType();

                             if (parameterType.isInterface()) {

                                 if (p.isAnnotationPresent(Inject.class)) {

                                     String qualifier = p.getAnnotation(Inject.class).qualifier();

                                     return context.getObject(qualifier)
                                                   .orElseGet(() -> {

                                                       Class<?> implClass = context.getConfig()
                                                                                   .getImplClassByQualifier(parameterType, qualifier)
                                                                                   .orElseThrow();

                                                       return context.getObject(implClass);

                                                   });

                                 }

                                 Class<?> implClass = context.getConfig()
                                                             .getSingleImplClass(p.getType())
                                                             .orElseThrow();

                                 return context.getObject(implClass);

                             }

                             return context.getObject(parameterType);

                     })
                     .toArray();

    }

}