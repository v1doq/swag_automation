package projects.com.swag.steps.loginStep;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.swag.components.loginComponent.LoginComponents;

import static settings.TestConfig.getProperty;

public class LoginStepSWAG {

    private LoginComponents components;
    private static final String USERNAME = getProperty("swag.login");
    private static final String PASSWORD = getProperty("swag.pass");

    public LoginStepSWAG(WebDriver driver) {
        this.components = new LoginComponents(driver);
    }

    @Step("Open landing page")
    public void openLandingPage() {
        components.open(getProperty("swag.url"));
    }

    public void authorization() {
        openLandingPage();
        login(USERNAME, PASSWORD);
    }

    @Step("Login with user credential")
    public void login(String username, String pass) {
        components.getLoginButton().click();
        components.getEmailInput().sendKeys(username);
        components.getPasswordInput().sendKeys(pass);
        components.getLogInButton().click();
    }

    @Step("Verify that user was successfully login")
    public boolean isUserLogin() {
        return components.getUserMenuButton().isDisplayed();
    }
}
