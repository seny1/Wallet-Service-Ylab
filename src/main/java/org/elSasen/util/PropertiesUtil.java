package org.elSasen.util;

import java.io.IOException;
import java.util.Properties;

public class PropertiesUtil {

    public static final Properties PROPERTIES = new Properties();

    static {
        loadProperties();
    }

    public PropertiesUtil() {
    }

    private static void loadProperties() {
        try (var inputStream = PropertiesUtil.class.getClassLoader().getResourceAsStream("db/changelog/liquibase.properties")) {
            PROPERTIES.load(inputStream);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static String get(String key) {
        return PROPERTIES.getProperty(key);
    }
}
