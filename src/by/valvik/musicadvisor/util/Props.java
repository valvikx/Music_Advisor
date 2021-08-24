package by.valvik.musicadvisor.util;

import by.valvik.musicadvisor.exception.ConfigurationException;

import java.io.IOException;
import java.io.InputStream;
import java.util.Arrays;
import java.util.Properties;

public final class Props {

    private static final String[] PROPERTIES_NAME = {"application", "messages"};

    private static final String PROPERTIES_EXT = ".properties";

    private static final Properties PROPERTIES = new Properties();

    private Props() {

    }

    static {

        Arrays.stream(PROPERTIES_NAME)
              .map(n -> n + PROPERTIES_EXT)
              .forEach(Props::loadProperties);

    }

    public static String getValue(String key) {

        return PROPERTIES.getProperty(key);

    }

    private static void loadProperties(String propertiesName) {

        ClassLoader classLoader = Props.class.getClassLoader();

        try (InputStream resource = classLoader.getResourceAsStream(propertiesName)) {

            PROPERTIES.load(resource);

        } catch (IOException e) {

            throw new ConfigurationException(e);

        }

    }
    
}
