package by.valvik.musicadvisor.util;

import by.valvik.musicadvisor.exception.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.NoSuchElementException;
import java.util.Properties;

public final class Props {

    private static final String APPLICATION_PROPERTIES = "application.properties";

    private static final String MESSAGES_PROPERTIES = "messages.properties";

    private static final Properties PROPERTIES = new Properties();

    public Props() {}

    static {

        loadProperties();

    }

    public static String getValue(String key) {

        return PROPERTIES.getProperty(key);

    }

    private static void loadProperties() {

        ClassLoader classLoader = Props.class.getClassLoader();

        try (InputStream application = classLoader.getResourceAsStream(APPLICATION_PROPERTIES);
             InputStream messages = classLoader.getResourceAsStream(MESSAGES_PROPERTIES)) {

            PROPERTIES.load(application);

            PROPERTIES.load(messages);

        } catch (IOException e) {

            throw new ConfigurationException(e);

        }

    }
    
}
