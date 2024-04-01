package connection;

import java.util.Properties;

public class AppConfig {
    private static final String CONFIG_FILE = "config.properties";

    private static Properties properties;

    static {
        properties = new Properties();
    }
    
    public static String getProperty(String key) {
        return properties.getProperty(key);
    }
}