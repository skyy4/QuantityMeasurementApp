package com.bridgelabz.quantitymeasurement;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Loads application configuration from {@code application.properties} on the
 * classpath and exposes typed accessors for database and repository settings.
 */
public class ApplicationConfig {

    private static final String PROPERTIES_FILE = "/application.properties";

    private static final ApplicationConfig INSTANCE = new ApplicationConfig();

    private final Properties properties = new Properties();

    public static ApplicationConfig getInstance() {
        return INSTANCE;
    }

    private ApplicationConfig() {
        try (InputStream in = getClass().getResourceAsStream(PROPERTIES_FILE)) {
            if (in != null) {
                properties.load(in);
            }
        } catch (IOException e) {
            // Fallback to defaults if properties cannot be loaded.
        }
    }

    public String getRepositoryType() {
        return getProperty("app.repository.type", "CACHE").trim().toUpperCase();
    }

    public String getDbDriverClassName() {
        return getProperty("db.driverClassName", "org.h2.Driver");
    }

    public String getDbUrl() {
        return getProperty("db.url", "jdbc:h2:~/quantity_measurement;AUTO_SERVER=TRUE");
    }

    public String getDbUsername() {
        return getProperty("db.username", "sa");
    }

    public String getDbPassword() {
        return getProperty("db.password", "");
    }

    public int getDbPoolMaxSize() {
        return Integer.parseInt(getProperty("db.pool.maxSize", "10"));
    }

    public int getDbPoolMinIdle() {
        return Integer.parseInt(getProperty("db.pool.minIdle", "2"));
    }

    public int getDbPoolMaxIdle() {
        return Integer.parseInt(getProperty("db.pool.maxIdle", "10"));
    }

    private String getProperty(String key, String defaultValue) {
        String sysValue = System.getProperty(key);
        if (sysValue != null && !sysValue.isEmpty()) {
            return sysValue;
        }
        return properties.getProperty(key, defaultValue);
    }
}

