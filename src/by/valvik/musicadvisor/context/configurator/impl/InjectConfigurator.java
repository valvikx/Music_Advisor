package by.valvik.musicadvisor.context.configurator.impl;

import by.valvik.musicadvisor.annotation.Inject;
import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.context.configurator.ObjectConfigurator;
import by.valvik.musicadvisor.exception.ConfigurationException;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.NoSuchElementException;

public class InjectConfigurator implements ObjectConfigurator {

    @Override
    public void configure(Object configurableObject, ApplicationContext context) {

        Arrays.stream(configurableObject.getClass().getDeclaredFields())
              .filter(f -> f.isAnnotationPresent(Inject.class))
              .forEach(f -> {

                  try {

                      f.setAccessible(true);

                      String qualifier = f.getAnnotation(Inject.class).qualifier();

                      Object object;

                      if (qualifier.isEmpty()) {

                          Class<?> fieldType = f.getType().isInterface() ? getSingleImpl(f, context) : f.getType();

                          object = context.getObject(fieldType);

                      } else {

                          object = context.getObject(qualifier)
                                          .orElseGet(() -> {

                                              Class<?> fieldType = f.getType().isInterface() ? getImplByQualifier(f, context, qualifier)
                                                                                             : f.getType();

                                              return context.getObject(fieldType);

                                          });

                      }

                      f.set(configurableObject, object);

                  } catch (IllegalAccessException | NoSuchElementException e) {

                      throw new ConfigurationException(e);

                  }

              });

    }

    private Class<?> getSingleImpl(Field field, ApplicationContext context) {

        return context.getConfig().getSingleImplClass(field.getType()).orElseThrow();

    }

    private Class<?> getImplByQualifier(Field field, ApplicationContext context, String qualifier) {

        return context.getConfig().getImplClassByQualifier(field.getType(), qualifier).orElseThrow();

    }

}
