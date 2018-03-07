package projects.com.password.tools.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class LoginComponent extends BaseComponent {

    public LoginComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getUsernameInput(){
        return $(name("login-page-username"));
    }

    public WebElement getPasswordInput(){
        return $(name("login-page-password"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("button"));
    }

    public WebElement getLogoutButton(){
        return $(cssSelector(".btn-logout.btn"));
    }

    public WebElement getServerError(){
        return $(cssSelector(".error-block"));
    }
}
