package by.valvik.musicadvisor;

import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.context.ObjectFactory;
import by.valvik.musicadvisor.context.config.Config;
import by.valvik.musicadvisor.context.config.impl.DefaultConfig;

public class Application {

    public static ApplicationContext run(String packagesPrefix) {

        Config config = new DefaultConfig(packagesPrefix);

        ApplicationContext context = new ApplicationContext(config);

        ObjectFactory objectFactory = new ObjectFactory(context);

        context.setObjectFactory(objectFactory);

        context.initSingletonObjects();

        return context;

    }

}
