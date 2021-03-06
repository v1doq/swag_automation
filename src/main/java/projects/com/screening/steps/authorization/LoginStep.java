package projects.com.screening.steps.authorization;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.screening.components.authorization.LoginComponent;

import java.util.List;

import static common.DefaultConstant.SCREENING_PASSWORD;
import static common.DefaultConstant.SCREENING_USERNAME;
import static java.util.Arrays.asList;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static projects.com.screening.steps.authorization.RegisterStep.MAX_PASSWORD_LENGTH;
import static projects.com.screening.steps.authorization.RegisterStep.MIN_PASSWORD_LENGTH;
import static settings.TestConfig.getProperty;

public class LoginStep {

    private LoginComponent component;

    public LoginStep(WebDriver driver) {
        this.component = new LoginComponent(driver);
    }

    @Step("Open landing page")
    public void openLandingPage() {
        component.open(getProperty("screening.url"));
    }

    @Step("Authorize by existing user")
    public void authorization(){
        openLandingPage();
        login(SCREENING_USERNAME, SCREENING_PASSWORD);
    }

    @Step("Login with user credential")
    public void login(String username, String pass) {
        while (!component.isElementPresent(cssSelector("span:nth-child(8)>button"))){
            component.getLoginButton().click();
            component.getUsernameInput().sendKeys(username);
            component.getPasswordInput().sendKeys(pass);
            component.getSubmitButton().click();
            component.assertThat(visibilityOf(component.getLogoutButton()));
        }
    }

    @Step("Log out")
    public void logout(){
        component.getLogoutButton().click();
    }

    @Step("Open login pop up")
    public void openLoginPopUp() {
        component.getLoginButton().click();
    }

    @Step("Fill username field")
    public void fillUsernameField(String username) {
        component.clearAndSendKeys(component.getUsernameInput(), username);
    }

    @Step("Fill password field")
    public void fillPasswordField(String pass) {
        component.clearAndSendKeys(component.getPasswordInput(), pass);
    }

    @Step("Submit login form")
    public void submitForm() {
        component.assertThat(elementToBeClickable(component.getSubmitButton()));
        component.assertThat(visibilityOf(component.getSubmitButton()));
        component.getSubmitButton().click();
    }

    public List<String> invalidPassList = asList(
            "  ",
            randomAlphabetic(MIN_PASSWORD_LENGTH - 1),
            randomAlphabetic(MAX_PASSWORD_LENGTH + 1)
    );

    @Step("Check server error message for invalid login or password")
    public boolean isServerErrorDisplayed(){
        String text = "login or password invalid";
        return component.getServerError(text).getText().contains(text);
    }

    @Step("Verify that the user is successfully authorized")
    public boolean isLogoutButtonDisplayed(){
        component.assertThat(visibilityOf(component.getLogoutButton()));
        return component.getLogoutButton().isDisplayed();
    }

    @Step("Check for the presence of the error message for username field")
    public boolean isUsernameErrorDisplayed() {
        component.assertThat(visibilityOf(component.getUsernameError()));
        return component.isUsernameErrorDisplayed();
    }

    @Step("Check for the presence of the error message for password field")
    public boolean isPasswordErrorDisplayed() {
        component.assertThat(visibilityOf(component.getPasswordError()));
        return component.isPasswordErrorDisplayed();
    }
}
