package steps.steps.authorization;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import steps.components.authorization.LoginComponent;

import static org.openqa.selenium.By.cssSelector;
import static settings.TestConfig.getProperty;

public class LoginStep {

    private LoginComponent component;

    public LoginStep(WebDriver driver) {
        this.component = new LoginComponent(driver);
    }

    @Step("Open landing page")
    private void openLandingPage() {
        component.open(getProperty("screening.url"));
    }

    @Step("Login with user credential")
    public void login(String userName, String pass) {
        while (!component.isElementPresent(cssSelector("span:nth-child(8)>button"))){
            component.getLoginButton().click();
            component.getLoginInput().sendKeys(userName);
            component.getPasswordInput().sendKeys(pass);
            component.getSubmitButton().click();
            component.sleep(1000);
        }
    }

    @Step("Log out")
    public void logout(){
        component.getLogoutButton().click();
    }

    @Step("Verify that the user is successfully authorized")
    public boolean isLogoutButtonDisplayed(){
        return component.getLogoutButton().isDisplayed();
    }

    @Step("Authorize by existing user")
    public void authorization(){
        openLandingPage();
        login(getProperty("login"), getProperty("pass"));
    }
}
