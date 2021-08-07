package by.valvik.musicadvisor.context.singleton;

import by.valvik.musicadvisor.context.annotation.Configuration;
import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.context.config.Config;
import by.valvik.musicadvisor.exception.ConfigurationException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static java.beans.Introspector.decapitalize;
import static java.util.function.Function.identity;
import static java.util.stream.Collectors.*;

public record SingletonScanner(Config config) {

    public Set<SingletonDefinition> scanConfigurationClasses() {

        Set<Class<?>> configClasses = config.getAnnotatedClasses(Configuration.class);

        if (configClasses.isEmpty()) {

            return Set.of();

        }

        Map<Class<?>, Set<Method>> metaData =
                configClasses.stream()
                             .collect(groupingBy(identity(),
                                                 flatMapping(c -> Arrays.stream(c.getDeclaredMethods())
                                                                        .filter(m -> m.isAnnotationPresent(Singleton.class)),
                                                             toSet())));

        if (metaData.isEmpty()) {

            return Set.of();

        }

        return metaData.entrySet()
                       .stream()
                       .flatMap(e -> {

                           try {

                               Object configObject = e.getKey().getDeclaredConstructor().newInstance();

                               return e.getValue().stream()
                                                  .map(m -> new SingletonDefinition(m.getName(),
                                                                                    m.getReturnType(),
                                                                                   null,
                                                                                    new SingletonMethod(configObject, m, m.getParameters())));


                           } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException ex) {

                               throw new ConfigurationException(ex);

                           }

                       })
                       .collect(toSet());

    }

    public Set<SingletonDefinition> scanAnnotatedClasses() {

        Set<Class<?>> singletonClasses = config.getAnnotatedClasses(Singleton.class);

        if (singletonClasses.isEmpty()) {

            return Set.of();

        }

        return singletonClasses.stream()
                               .map(c -> {

                                   Optional<Constructor<?>> injectCtor = Arrays.stream(c.getDeclaredConstructors())
                                                                               .filter(ctor -> ctor.isAnnotationPresent(Inject.class))
                                                                               .findFirst();

                                   Optional<Constructor<?>> constructor = injectCtor.or(() -> {

                                                                   try {

                                                                       return Optional.of(c.getDeclaredConstructor());

                                                                   } catch (NoSuchMethodException e) {

                                                                       throw new ConfigurationException(e);

                                                                   }

                                   });

                                   SingletonCtor singletonCtor = new SingletonCtor(constructor.orElseThrow());

                                   String qualifier = c.getDeclaredAnnotation(Singleton.class).qualifier();

                                   return new SingletonDefinition(qualifier.isEmpty() ? decapitalize(c.getSimpleName()) : qualifier,
                                                                  c,
                                                                  singletonCtor, 
                                                                 null);

                               })
                               .collect(toSet());

    }

}
                                            