package stepdefinitions;

import driver.DriverFactory;
import io.cucumber.java.en.*;
import org.testng.Assert;
import pages.MTVCalculatorPage;

public class MTVSteps {

    MTVCalculatorPage mtvPage = new MTVCalculatorPage(DriverFactory.getDriver());

    @Given("user navigates to MTV calculator page")
    public void userNavigatesToMTVCalculatorPage() {
        mtvPage.navigateToPage();
    }

    @When("user selects {string} in {string}")
    public void userSelectsInDropdown(String value, String label) {
        mtvPage.selectOption(label, value);
    }

    @And("user selects {string} for {string}")
    public void userSelectsForField(String value, String label) {
        mtvPage.selectOption(label, value);
    }

    @And("user clicks the Calculate button")
    public void userClicksTheCalculateButton() {
        mtvPage.clickCalculateButton();
    }

    @Then("the result should be displayed")
    public void theResultShouldBeDisplayed() {
        Assert.assertTrue(mtvPage.isResultVisible(), "Expected result to be visible, but it was not.");
    }

    @Then("the first half-year payment should be visible")
    public void theFirstHalfYearPaymentShouldBeVisible() {
        Assert.assertTrue(mtvPage.isFirstHalfVisible(), "First half-year amount should be visible.");
    }

    @Then("the second half-year payment should be visible")
    public void theSecondHalfYearPaymentShouldBeVisible() {
        Assert.assertTrue(mtvPage.isSecondHalfVisible(), "Second half-year amount should be visible.");
    }

    @Then("the total payment should be {string}")
    public void theTotalPaymentShouldBe(String expected) {
        Assert.assertEquals(mtvPage.getTotalAmount(), expected, "Total amount does not match the expected value.");
    }

    @Then("the first half-year payment should be {string}")
    public void theFirstHalfYearPaymentShouldBe(String expected) {
        Assert.assertEquals(mtvPage.getFirstHalfAmount(), expected, "First half-year amount does not match the expected value.");
    }

    @Then("the second half-year payment should be {string}")
    public void theSecondHalfYearPaymentShouldBe(String expected) {
        Assert.assertEquals(mtvPage.getSecondHalfAmount(), expected, "Second half-year amount does not match the expected value.");
    }

    @Then("{string} dropdown should be {string}")
    public void dropdownVisibilityCheck(String fieldLabel, String expectedVisibility) {
        boolean isVisible = mtvPage.isDropdownVisible(fieldLabel);
        if (expectedVisibility.equals("visible")) {
            Assert.assertTrue(isVisible, "Expected '" + fieldLabel + "' dropdown to be visible, but it was not.");
        } else {
            Assert.assertFalse(isVisible, "Expected '" + fieldLabel + "' dropdown to be hidden, but it was visible.");
        }
    }

    @Then("{string} dropdown should be visible")
    public void dropdown_should_be_visible(String labelText) {
        boolean isVisible = mtvPage.isDropdownVisible(labelText);
        Assert.assertTrue(isVisible, "Dropdown should be visible: " + labelText);
    }

    @Then("{string} dropdown should be hidden")
    public void dropdown_should_be_hidden(String labelText) {
        boolean isVisible = mtvPage.isDropdownVisible(labelText);
        Assert.assertFalse(isVisible, "Dropdown should be hidden: " + labelText);
    }

    @Then("the result should not be displayed")
    public void the_result_should_not_be_displayed() {
        boolean isResultVisible = mtvPage.isResultVisible();
        Assert.assertFalse(isResultVisible, "Result section should not be visible, but it is.");
    }

    @And("error message {string} should be visible")
    public void error_message_should_be_visible(String expectedMessage) {
        Assert.assertTrue(mtvPage.isErrorMessageDisplayed(expectedMessage),
                "Expected error message not found: " + expectedMessage);
    }
}