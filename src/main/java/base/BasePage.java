package base;

import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.remote.RemoteWebDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.LocatorReader;

import java.time.Duration;

public abstract class BasePage {

    private static final Logger log = LoggerFactory.getLogger(BasePage.class);

    protected WebDriver driver;
    protected WebDriverWait wait;

    public BasePage(WebDriver driver) {
        this.driver = driver;
        this.wait = new WebDriverWait(driver, Duration.ofSeconds(10));
    }

    public void clickWithJs(WebElement element) {
        scrollToElement(element);
        ((JavascriptExecutor) driver).executeScript("arguments[0].click();", element);
    }

    public void scrollToElement(WebElement element) {
        ((JavascriptExecutor) driver).executeScript("arguments[0].scrollIntoView({block: 'center'});", element);
    }

    public void waitUntilVisible(By by) {
        wait.until(ExpectedConditions.visibilityOfElementLocated(by));
    }

    public String getTextSafe(By by) {
        if (by == null) {
            log.warn("getTextSafe: Locator is null.");
            return "";
        }

        try {
            WebElement element = driver.findElement(by);
            String text = element.getText().trim();
            if (text.isEmpty()) {
                log.warn("getTextSafe: Element found but text is empty. Locator: {}", by);
            }
            return text;
        } catch (StaleElementReferenceException e) {
            WebElement element = wait.until(ExpectedConditions.presenceOfElementLocated(by));
            String text = element.getText().trim();
            if (text.isEmpty()) {
                log.warn("getTextSafe (Retry): Element found but text is empty. Locator: {}", by);
            }
            return text;
        } catch (NoSuchElementException e) {
            log.error("getTextSafe: Element not found. Locator: {}", by, e);
            return "";
        }
    }

    public void selectDropdownOption(String labelText, String value, LocatorReader locatorReader) {
        if (labelText == null || labelText.trim().isEmpty() ||
                value == null || value.trim().isEmpty() ||
                locatorReader == null) {
            log.error("Dropdown selection failed: Invalid parameters. labelText: '{}', value: '{}', locatorReader null? {}",
                    labelText, value, (locatorReader == null));
            throw new IllegalArgumentException("Invalid parameters for dropdown selection.");
        }

        try {
            log.info("Opening dropdown: {}", labelText);

            String browser = ((RemoteWebDriver) driver).getCapabilities().getBrowserName().toLowerCase();
            new Actions(driver).sendKeys(Keys.ESCAPE).perform();

            if (browser.contains("firefox")) {
                try {
                    String closeMenuXpath = locatorReader.get("dropdown.closeMenu");
                    WebDriverWait shortWait = new WebDriverWait(driver, Duration.ofSeconds(2));
                    shortWait.until(ExpectedConditions.invisibilityOfElementLocated(By.xpath(closeMenuXpath)));
                } catch (TimeoutException ignored) {
                }
                Thread.sleep(200);
            }

            String dropdownXPath = locatorReader.get("dropdown.label", labelText);
            WebElement dropdown = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(dropdownXPath)));
            dropdown.click();

            if (browser.contains("firefox")) {
                Thread.sleep(800);
            }

            String listLocator = locatorReader.get("dropdown.list.visible");
            wait.until(ExpectedConditions.presenceOfElementLocated(By.xpath(listLocator)));

            String optionXPath = locatorReader.get("dropdown.option", labelText, value);
            WebElement option = new WebDriverWait(driver, Duration.ofSeconds(4))
                    .until(ExpectedConditions.elementToBeClickable(By.xpath(optionXPath)));

            scrollToElement(option);
            clickWithJs(option);

            log.info("Dropdown selected → {} : {}", labelText, value);
        } catch (Exception e) {
            log.error("Dropdown selection failed! Label: {} - Value: {}", labelText, value, e);
            throw new RuntimeException("Dropdown selection failed: " + labelText + " -> " + value, e);
        }
    }

    public void clickButtonByLocatorKey(String locatorKey, LocatorReader locatorReader) {
        try {
            String locatorRaw = locatorReader.get(locatorKey);
            WebElement button;

            if (locatorRaw.startsWith("id=")) {
                String id = locatorRaw.replace("id=", "");
                button = wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
            } else {
                button = wait.until(ExpectedConditions.elementToBeClickable(By.xpath(locatorRaw)));
            }

            clickWithJs(button);
            log.info("Button clicked → {}", locatorKey);
        } catch (Exception e) {
            log.error("Failed to click button! LocatorKey: {}", locatorKey, e);
            throw new RuntimeException("Button click failed: " + locatorKey, e);
        }
    }

    public boolean isDropdownVisible(String labelText, LocatorReader locatorReader) {
        try {
            String xpath = locatorReader.get("dropdown.combo", labelText);
            WebElement dropdown = driver.findElement(By.xpath(xpath));
            return dropdown.isDisplayed();
        } catch (NoSuchElementException | TimeoutException e) {
            return false;
        }
    }
}
