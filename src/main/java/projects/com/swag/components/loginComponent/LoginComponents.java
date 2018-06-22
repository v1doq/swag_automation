package projects.com.swag.components.loginComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class LoginComponents extends SignUpEmpComponents {

    public LoginComponents(WebDriver driver) {
        super(driver);
    }

    public WebElement getLoginButton() {
        return $(linkText("/login"));
    }

    public WebElement getRememberButton() {
        return $(cssSelector("div.line-control-content > label"));
    }

    public WebElement getLogInButton() {
        return $(cssSelector("[type='button']"));
    }

    public WebElement getForgotPassword() {
        return $(partialLinkText("Forgot Pass"));
    }

    public WebElement getUserMenuButton() {
        return $(className("react-header-profile"));
    }

    public WebElement getProfileButton() {
        return $(partialLinkText("/profile"));
    }

    public WebElement getSettingsButton() {
        return $(partialLinkText("/settings"));
    }

    public WebElement getContractButton() {
        return $(cssSelector("div.header-profile-action > a:nth-child(3)"));
    }

    public WebElement getLogoutButton() {
        return $(cssSelector("div.header-profile-action > a:nth-child(4)"));
    }
}
