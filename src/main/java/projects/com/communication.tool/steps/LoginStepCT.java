package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.LoginComponentCT;

import static common.ConciseApi.sleep;
import static common.DefaultConstant.PASSWORD_COMMUNICATION_TOOLS;
import static common.DefaultConstant.USERNAME_COMMUNICATION_TOOLS;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static settings.TestConfig.getProperty;

public class LoginStepCT {

    private LoginComponentCT component;

    public LoginStepCT(WebDriver driver) {
        this.component = new LoginComponentCT(driver);
    }

    public void authorization(){
        openLandingPage();
        login(USERNAME_COMMUNICATION_TOOLS, PASSWORD_COMMUNICATION_TOOLS);
        sleep(2000);
    }

    @Step("Open landing page")
    public void openLandingPage() {
        component.open(getProperty("communication.tool.url"));
    }

    @Step("Login with user credential")
    public void login(String username, String pass) {
        component.getEmailInput().sendKeys(username);
        component.getPasswordInput().sendKeys(pass);
        component.getSubmitButton().click();
    }

    @Step("Log out")
    public void logout() {
        component.assertThat(visibilityOf(component.getUserMenuButton()));
        component.getUserMenuButton().click();
        component.getLogoutButton().click();
    }

    @Step("Verify that user was successfully login")
    public boolean isUserLogin(){
        return component.getLogoutButton().isDisplayed();
    }
}
