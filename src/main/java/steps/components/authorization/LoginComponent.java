package steps.components.authorization;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import steps.components.base.BaseComponent;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;

public class LoginComponent extends BaseComponent {

    public LoginComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getLoginButton(){
        return $(cssSelector(".btn--login"));
    }

    public WebElement getLoginInput(){
        return $(xpath("//form/div/div/input"));
    }

    public WebElement getPasswordInput(){
        return $(xpath("//form/div[2]/div/input"));
    }

    public WebElement getSubmitButton(){
        return $(xpath("//div[4]/button[2]"));
    }

    public WebElement getLogoutButton(){
        return $(cssSelector("span:nth-child(8)>button"));
    }
}
