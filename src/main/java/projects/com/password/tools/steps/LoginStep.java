package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.LoginComponent;

import static common.DefaultConstant.PASSWORD_PASS_TOOLS;
import static common.DefaultConstant.USERNAME_PASS_TOOLS;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;
import static settings.TestConfig.getProperty;

public class LoginStep {

    private LoginComponent component;

    public LoginStep(WebDriver driver) {
        this.component = new LoginComponent(driver);
    }

    @Step("Authorize by existing user")
    public void authorization(){
        openLandingPage();
        login(USERNAME_PASS_TOOLS, PASSWORD_PASS_TOOLS);
    }

    @Step("Open landing page")
    public void openLandingPage() {
        component.open(getProperty("password.tools.url"));
    }

    @Step("Login with user credential")
    public void login(String username, String pass) {
        component.getUsernameInput().sendKeys(username);
        component.getPasswordInput().sendKeys(pass);
        component.getSubmitButton().click();
        component.assertThat(textToBe(className("toolbar__title"), "Passwords Tool"));
    }

    @Step("Log out")
    public void logout() {
        component.getDriver().manage().window().maximize();
        component.getLogoutButton().click();
    }

    @Step("Verify that user was successfully login")
    public boolean isUserLogin(){
        return component.getLogoutButton().isDisplayed();
    }
}
