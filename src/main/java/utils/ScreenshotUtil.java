package utils;

import io.qameta.allure.Allure;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;

public class ScreenshotUtil {

    public static void attachScreenshot(WebDriver driver) {
        try {
            if (driver == null) {
                System.err.println("WebDriver is null!");
                return;
            }

            byte[] screenshot = ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
            Allure.getLifecycle().addAttachment(" Screenshot", "image/png", "png", screenshot);
            System.out.println("Screenshot added");
        } catch (Exception e) {
            System.err.println("Could not take screenshot:" + e.getMessage());
        }
    }
}
