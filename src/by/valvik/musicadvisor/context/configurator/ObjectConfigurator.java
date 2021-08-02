package by.valvik.musicadvisor.context.configurator;

import by.valvik.musicadvisor.context.ApplicationContext;

public interface ObjectConfigurator {

    void configure(Object configurableObject, ApplicationContext context);

}
