package by.valvik.musicadvisor.context;

import by.valvik.musicadvisor.context.config.Config;
import by.valvik.musicadvisor.context.singleton.SingletonDefinition;
import by.valvik.musicadvisor.context.singleton.SingletonScanner;

import java.util.*;

import static java.util.Optional.*;
import static java.util.stream.Collectors.toCollection;

public class ApplicationContext {

    private final Map<String, Object> singletons;

    private final Map<String, SingletonDefinition> singletonDefinitions;

    private ObjectFactory objectFactory;

    private final Config config;

    public ApplicationContext(Config config) {

        this.config = config;

        this.singletons = new HashMap<>();

        this.singletonDefinitions = new HashMap<>();

    }

    public void createSingletons() {

        SingletonScanner scanner = new SingletonScanner(config);

        Set<SingletonDefinition> definitions = scanner.scanConfigurationClasses();

        definitions.addAll(scanner.scanAnnotatedClasses());

        definitions.forEach(d -> singletonDefinitions.put(d.name(), d));

        createSingletonsByDefinition();

    }

    public <T> T getObject(Class<T> tClass) {

        Optional<T> foundSingleton = findSingletonByClass(tClass);

        return foundSingleton.orElseGet(() -> {

           Optional<SingletonDefinition> foundDefinitions = findSingletonDefinitionByClass(tClass);

           return foundDefinitions.map(d -> {

                                       T t = (T) objectFactory.create(d);

                                       addSingleton(d.name(), t);

                                       return t;

                                  })
                                  .orElseGet(() -> {

                                      Class<T> implClass = tClass.isInterface() ? getSingleImpl(tClass) : tClass;

                                      return objectFactory.create(implClass);

                                  });

        });

    }

    public <T> T getObject(String qualifier, Class<T> tClass) {

        Optional<Object> singletonOptional = ofNullable(singletons.get(qualifier));

        return tClass.cast(singletonOptional.orElseGet(() -> {

            SingletonDefinition definition = ofNullable(singletonDefinitions.get(qualifier)).orElseThrow();

            Object singleton = objectFactory.create(definition);

            addSingleton(definition.name(), singleton);

            return singleton;

        }));

    }

    public void setObjectFactory(ObjectFactory objectFactory) {

        this.objectFactory = objectFactory;

    }

    public Object addSingleton(String qualifier, Object singleton) {

         if (singletons.containsKey(qualifier)) {

              return null;

         }

         return singletons.put(qualifier, singleton);

    }

    public Config getConfig() {

        return config;

    }

    private void createSingletonsByDefinition() {

        singletonDefinitions.values().forEach(d -> {

            Object singleton = objectFactory.create(d);

            addSingleton(d.name(), singleton);

        });

    }

    private <T> Optional<T> findSingletonByClass(Class<T> clazz) {

          return queueWrapper((Queue<T>) singletons.values()
                                                   .stream()
                                                   .filter(o -> Objects.equals(o.getClass(), clazz))
                                                   .collect(toCollection(ArrayDeque::new)));


    }

    private Optional<SingletonDefinition> findSingletonDefinitionByClass(Class<?> clazz) {

        return queueWrapper(singletonDefinitions.values()
                                                .stream()
                                                .filter(c -> Objects.equals(c.singletonClass(), clazz))
                                                .collect(toCollection(ArrayDeque::new)));

    }

    private <T> Optional<T> queueWrapper(Queue<T> queue) {

        if (queue.size() != 1) {

            return empty();

        }

        return of(queue.poll());

    }

    private <T> Class<T> getSingleImpl(Class<T> implClass) {

        return (Class<T>) config.getSingleImplClass(implClass).orElseThrow();

    }

}