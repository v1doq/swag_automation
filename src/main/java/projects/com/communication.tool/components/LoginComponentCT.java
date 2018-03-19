package projects.com.communication.tool.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class LoginComponentCT extends BaseComponent {

    public LoginComponentCT(WebDriver driver) {
        super(driver);
    }

    public WebElement getEmailInput(){
        return $(name("email"));
    }

    public WebElement getPasswordInput(){
        return $(name("password"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("button"));
    }

    public WebElement getUserBlockButton(){
        return $(className("btn-user-block"));
    }

    public WebElement getLogoutButton(){
        return $(className(".menu-popup-link"));
    }
}
