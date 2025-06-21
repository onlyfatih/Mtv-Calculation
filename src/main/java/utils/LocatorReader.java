package utils;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;

public class LocatorReader {

    private static final String LOCATOR_PATH = "src/main/resources/locators/";
    private final Properties properties = new Properties();

    public LocatorReader(String pageName) {
        try (FileInputStream fis = new FileInputStream(LOCATOR_PATH + pageName + ".properties")) {
            properties.load(fis);
        } catch (IOException e) {
            throw new RuntimeException("Could not load locator file for page: " + pageName, e);
        }
    }

    public String get(String key, String... replacements) {
        String value = properties.getProperty(key);
        if (value == null) throw new RuntimeException("Locator not found: " + key);

        if (replacements.length > 0) {
            value = value.replace("{{label}}", replacements[0]);
        }
        if (replacements.length > 1) {
            value = value.replace("{{value}}", replacements[1]);
        }

        return value;
    }
}
