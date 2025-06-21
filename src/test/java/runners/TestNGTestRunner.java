package runners;

import driver.DriverFactory;
import io.cucumber.testng.AbstractTestNGCucumberTests;
import io.cucumber.testng.CucumberOptions;
import org.testng.ITestContext;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@CucumberOptions(
        features = "src/test/resources/features",
        glue = {"stepdefinitions", "hooks"},
        plugin = {
                "pretty",
                "io.qameta.allure.cucumber7jvm.AllureCucumber7Jvm",
                "html:target/cucumber-report.html",
                "json:target/cucumber.json"
        },
        monochrome = true
)
public class TestNGTestRunner extends AbstractTestNGCucumberTests {

    private static final Logger log = LoggerFactory.getLogger(TestNGTestRunner.class);

    @BeforeClass(alwaysRun = true)
    public void beforeClass(ITestContext context) {
        String browser = context.getCurrentXmlTest().getParameter("browser");
        String allureResultsDir = context.getCurrentXmlTest().getParameter("allureResultsDir");
        String tags = context.getCurrentXmlTest().getParameter("cucumber.filter.tags");

        if (browser == null) browser = "chrome";
        if (allureResultsDir == null) allureResultsDir = "target/allure-results";

        System.setProperty("browser", browser);
        System.setProperty("allure.results.directory", allureResultsDir);

        if (tags != null) {
            System.setProperty("cucumber.filter.tags", tags);
        }

        log.info(" Browser: {}", browser);
        log.info(" Allure Output Dir: {}", allureResultsDir);
        log.info(" Tags: {}", tags);
    }


    @Override
    @DataProvider(parallel = true)
    public Object[][] scenarios() {
        return super.scenarios();
    }
}
