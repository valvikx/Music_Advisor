package by.valvik.musicadvisor;

import by.valvik.musicadvisor.context.ApplicationContext;
import by.valvik.musicadvisor.context.ObjectFactory;
import by.valvik.musicadvisor.context.config.Config;
import by.valvik.musicadvisor.context.config.impl.DefaultConfig;

public class Application {

    public static ApplicationContext run(String packagesPrefix) {

        ApplicationContext initialContext = new ApplicationContext();

        Config config = new DefaultConfig(packagesPrefix);

        initialContext.setConfig(config);

        ObjectFactory objectFactory = new ObjectFactory(initialContext);

        initialContext.setObjectFactory(objectFactory);

        initialContext.createSingletons();

        ApplicationContext applicationContext = initialContext.getObject(ApplicationContext.class);

        initialContext.copyTo(applicationContext);

        return applicationContext;

    }

}
