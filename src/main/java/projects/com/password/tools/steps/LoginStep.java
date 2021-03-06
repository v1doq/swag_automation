package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.LoginComponent;

import static common.DefaultConstant.PASSWORD_PASS_TOOLS;
import static common.DefaultConstant.USERNAME_PASS_TOOLS;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
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
        component.getPasswordInput().sendKeys(Keys.ENTER);
        component.getSubmitButton().click();
    }

    @Step("Log out")
    public void logout() {
        component.assertThat(visibilityOf(component.getLogoutButton()));
        component.getLogoutButton().click();
    }

    @Step("Check server error message for invalid login or password")
    public boolean isServerErrorDisplayed(){
        String text = "Incorrect login or password";
        return component.getServerError().getText().contains(text);
    }

    @Step("Verify that user was successfully login")
    public boolean isUserLogin(){
        return component.getLogoutButton().isDisplayed();
    }
}
