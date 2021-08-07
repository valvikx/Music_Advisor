package by.valvik.musicadvisor;

import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.context.ObjectFactory;
import by.valvik.musicadvisor.context.config.Config;
import by.valvik.musicadvisor.context.config.impl.DefaultConfig;

public class Application {

    public static ApplicationContext run(String packagesPrefix) {

        ApplicationContext context = new ApplicationContext();

        Config config = new DefaultConfig(packagesPrefix);

        context.setConfig(config);

        ObjectFactory objectFactory = new ObjectFactory(context);

        context.setObjectFactory(objectFactory);

        context.createSingletons();

        return context;

    }

}
