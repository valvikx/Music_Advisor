package by.valvik.musicadvisor.context;

import by.valvik.musicadvisor.context.annotation.Singleton;
import by.valvik.musicadvisor.context.config.Config;
import by.valvik.musicadvisor.context.singleton.SingletonDefinition;
import by.valvik.musicadvisor.context.singleton.SingletonScanner;
import by.valvik.musicadvisor.exception.ConfigurationException;

import java.util.*;

import static java.util.Optional.*;
import static java.util.stream.Collectors.toCollection;

@Singleton
public class ApplicationContext {

    private final Map<String, Object> singletons;

    private final Map<String, SingletonDefinition> singletonDefinitions;

    private ObjectFactory objectFactory;

    private Config config;

    public ApplicationContext() {

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

    public void mergeContexts(ApplicationContext context) {

        Arrays.stream(context.getClass().getDeclaredFields())
              .forEach(f -> {

                  try {

                       f.setAccessible(true);

                       f.set(context, f.get(this));

                  } catch (IllegalAccessException e) {

                      throw new ConfigurationException(e);

                  }

              });

    }

    public ObjectFactory getObjectFactory() {

        return objectFactory;

    }

    public void setObjectFactory(ObjectFactory objectFactory) {

        this.objectFactory = objectFactory;

    }

    public Config getConfig() {

        return config;

    }

    public void setConfig(Config config) {

        this.config = config;

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

    private void addSingleton(String qualifier, Object singleton) {

        if (singletons.containsKey(qualifier)) {

            return;

        }

        singletons.put(qualifier, singleton);

    }

}