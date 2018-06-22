package projects.com.screening.components.authorization;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import common.BaseComponent;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class LoginComponent extends BaseComponent {

    public LoginComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getLoginButton(){
        return $(cssSelector(".btn--login"));
    }

    public WebElement getUsernameInput(){
        return $(xpath("//form/div/div/input"));
    }

    public WebElement getUsernameError(){
        return $(xpath("//form/div/div[2]/div/div"));
    }

    public WebElement getPasswordInput(){
        return $(xpath("//form/div[2]/div/input"));
    }

    public WebElement getPasswordError(){
        return $(xpath("//div[2]/div[2]/div/div"));
    }

    public WebElement getSubmitButton(){
        return $(xpath("//div[4]/button[2]"));
    }

    public WebElement getLogoutButton(){
        return $(cssSelector("span:nth-child(8)>button"));
    }

    public WebElement getServerError(String text){
        assertThat(textToBe(cssSelector(".dialog__content__active>div>div>div.red--text"), text));
        return $(cssSelector(".dialog__content__active>div>div>div.red--text"));
    }

    public boolean isUsernameErrorDisplayed(){
        return isElementPresent(xpath("//form/div/div[2]/div/div"));
    }

    public boolean isPasswordErrorDisplayed(){
        return isElementPresent(xpath("//div[2]/div[2]/div/div"));
    }
}
