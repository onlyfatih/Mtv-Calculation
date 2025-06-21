package hooks;

import driver.DriverFactory;
import io.cucumber.java.After;
import io.cucumber.java.Before;
import io.cucumber.java.Scenario;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigReader;
import utils.ScreenshotUtil;

import java.io.FileWriter;
import java.io.IOException;
import java.util.Properties;

public class Hooks {

    private static final Logger log = LoggerFactory.getLogger(Hooks.class);
    private static boolean metadataWritten = false;

    @Before
    public void setUp(Scenario scenario) {
        String browser = System.getProperty("browser", "chrome");
        String environment = System.getProperty("env", "local");
        String resultsDir = System.getProperty("allureResultsDir", "target/allure-results/chrome");

        System.setProperty("allure.results.directory", resultsDir);

        log.info("Starting scenario: {}", scenario.getName());
        log.info("Browser: {}, Environment: {}", browser, environment);

        if (!metadataWritten) {
            writeEnvironmentProperties(resultsDir, browser, environment);
            writeExecutorJson(resultsDir);
            writeCategoriesJson(resultsDir);
            metadataWritten = true;
        }

        DriverFactory.setBrowser(browser);
        DriverFactory.initDriver();
    }

    @After
    public void tearDown(Scenario scenario) {
        if (scenario.isFailed()) {
            ScreenshotUtil.attachScreenshot(DriverFactory.getDriver());
        }
        DriverFactory.quitDriver();
    }

    private void writeEnvironmentProperties(String resultsDir, String browser, String environment) {
        Properties props = new Properties();
        props.setProperty("Test Engineer", ConfigReader.get("test.engineer"));
        props.setProperty("Organization", ConfigReader.get("organization"));
        props.setProperty("Project", ConfigReader.get("project.name"));
        props.setProperty("Browser", browser);
        props.setProperty("Environment", environment);
        props.setProperty("Operating System", System.getProperty("os.name"));
        props.setProperty("Java Version", System.getProperty("java.version"));
        props.setProperty("Framework", "Cucumber, Selenium, TestNG");
        props.setProperty("Report Generated At", java.time.LocalDateTime.now().toString());

        try (FileWriter writer = new FileWriter(resultsDir + "/environment.properties")) {
            props.store(writer, "Allure Environment Properties");
            log.info("environment.properties created at {}", resultsDir);
        } catch (IOException e) {
            log.error("Failed to write environment.properties", e);
        }
    }

    private void writeExecutorJson(String resultsDir) {
        String content = String.format("""
                {
                  "name": "%s",
                  "type": "%s",
                  "reportName": "%s",
                  "buildName": "%s",
                  "buildUrl": "%s",
                  "reportUrl": "%s"
                }
                """,
                ConfigReader.get("executor.name"),
                ConfigReader.get("executor.type"),
                ConfigReader.get("executor.reportName"),
                ConfigReader.get("executor.buildName"),
                ConfigReader.get("executor.buildUrl"),
                ConfigReader.get("executor.reportUrl"));

        try (FileWriter writer = new FileWriter(resultsDir + "/executor.json")) {
            writer.write(content);
            log.info("executor.json created at {}", resultsDir);
        } catch (IOException e) {
            log.error("Failed to write executor.json", e);
        }
    }

    private void writeCategoriesJson(String resultsDir) {
        String resourcePath = "src/main/resources/categories.json";
        try (FileWriter writer = new FileWriter(resultsDir + "/categories.json")) {
            String content = new String(java.nio.file.Files.readAllBytes(java.nio.file.Paths.get(resourcePath)));
            writer.write(content);
            log.info("categories.json created at {}", resultsDir);
        } catch (IOException e) {
            log.error("Failed to write categories.json from resource", e);
        }
    }
}
