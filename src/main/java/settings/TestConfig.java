package settings;

import java.io.IOException;
import java.net.URL;
import java.util.Properties;

public class TestConfig {
    private static Properties properties;

    static {
        properties = new Properties();
        URL props = ClassLoader.getSystemResource("config.properties");
        try {
            properties.load(props.openStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static String getProperty(String key){
        return properties.getProperty(key);
    }

    private static String browser = getProperty("browser.name");

    public static Browser getBrowser() {
        Browser browserForTests = Browser.getByName(browser);
        if (browserForTests != null) {
            return browserForTests;
        } else {
            throw new IllegalStateException("settings.Browser name '" + browser + "' is not valid");
        }
    }
}
