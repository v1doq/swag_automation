package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.LoginComponentCT;

import static common.ConciseApi.sleep;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;
import static settings.TestConfig.getProperty;

public class LoginStepCT {

    private LoginComponentCT component;
    private static final String USERNAME = getProperty("communication.tool.login");
    private static final String PASSWORD = getProperty("communication.tool.pass");

    public LoginStepCT(WebDriver driver) {
        this.component = new LoginComponentCT(driver);
    }

    public void authorization(){
        openLandingPage();
        login(USERNAME, PASSWORD);
        sleep(2000);
    }

    @Step("Open landing page")
    public void openLandingPage() {
        component.open(getProperty("communication.tool.url"));
    }

    @Step("Login with user credential")
    public void login(String username, String pass) {
        component.jsClearAndSendKeys(component.getEmailInput(), username);
        component.jsClearAndSendKeys(component.getPasswordInput(), pass);
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
        return component.getUserMenuButton().isDisplayed();
    }
}
