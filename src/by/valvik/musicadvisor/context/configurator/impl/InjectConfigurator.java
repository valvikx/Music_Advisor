package by.valvik.musicadvisor.context.configurator.impl;

import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.context.annotation.Inject;
import by.valvik.musicadvisor.context.configurator.ObjectConfigurator;
import by.valvik.musicadvisor.exception.ConfigurationException;

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

                      Object injectObject = qualifier.isEmpty() ? context.getObject(f.getType())
                                                                : context.getObject(qualifier, f.getType());

                      f.set(configurableObject, injectObject);

                  } catch (IllegalAccessException | NoSuchElementException e) {

                      throw new ConfigurationException(e);

                  }

              });

    }

}
