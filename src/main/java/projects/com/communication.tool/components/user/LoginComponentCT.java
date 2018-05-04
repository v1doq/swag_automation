package projects.com.communication.tool.components.user;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

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

    public WebElement getUserMenuButton(){
        return $(cssSelector("div.nav-right.is-flex.user-block > div > div > button"));
    }

    public WebElement getLogoutButton(){
        return $(cssSelector(".menuable__content__active > div > div:nth-child(2) > a"));
    }
}
