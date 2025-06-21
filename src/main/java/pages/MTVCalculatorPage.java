package pages;

import base.BasePage;
import org.openqa.selenium.*;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import utils.ConfigReader;
import utils.LocatorReader;

import java.time.Duration;

public class MTVCalculatorPage extends BasePage {

    private final LocatorReader locatorReader = new LocatorReader("MTVCalculatorPage");

    public MTVCalculatorPage(WebDriver driver) {
        super(driver);
    }

    public void navigateToPage() {
        String url = ConfigReader.get("base.url");
        driver.get(url);

        String readyId = locatorReader.get("page.ready.id");
        wait.until(ExpectedConditions.visibilityOfElementLocated(By.id(readyId)));
    }

    public void selectOption(String labelText, String value) {
        selectDropdownOption(labelText, value, locatorReader);
    }

    public void clickCalculateButton() {
        clickButtonByLocatorKey("calculate.button", locatorReader);
    }

    public boolean isFirstHalfVisible() {
        String locator = locatorReader.get("result.firstHalf");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator))).isDisplayed();
    }

    public boolean isSecondHalfVisible() {
        String locator = locatorReader.get("result.secondHalf");
        return wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locator))).isDisplayed();
    }

    public String getTotalAmount() {
        String locator = locatorReader.get("result.total");
        return getTextSafe(By.xpath(locator));
    }

    public String getFirstHalfAmount() {
        String locator = locatorReader.get("result.first");
        return getTextSafe(By.xpath(locator));
    }

    public String getSecondHalfAmount() {
        String locator = locatorReader.get("result.second");
        return getTextSafe(By.xpath(locator));
    }

    public boolean isDropdownVisible(String labelText) {
        return isDropdownVisible(labelText, locatorReader);
    }

    public boolean isResultVisible() {
        try {
            WebElement result = driver.findElement(By.xpath(locatorReader.get("result.visible")));
            return result.isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }

    public boolean isErrorMessageDisplayed(String message) {
        try {
            return driver.findElement(By.xpath("//*[contains(text(),'" + message + "')]")).isDisplayed();
        } catch (NoSuchElementException e) {
            return false;
        }
    }
}
