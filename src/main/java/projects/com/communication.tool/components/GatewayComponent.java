package projects.com.communication.tool.components;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class GatewayComponent extends BaseComponent {

    public GatewayComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getGatewaysTab(){
        return $(cssSelector("div.tabs> ul>li:nth-child(2)"));
    }

    public WebElement getFromNameInput(){
        return $(name("name"));
    }

    public WebElement getFromEmailInput(){
        return $(name("email"));
    }

    public WebElement getHostInput(){
        return $(name("host"));
    }

    public WebElement getPortInput(){
        return $(name("port"));
    }

    public WebElement getLoginInput(){
        return $(name("login"));
    }

    public WebElement getPasswordInput(){
        return $(name("password"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("button.btn-save.btn-medium"));
    }

    public By getFromNameValue(){
        return by(cssSelector(".card__textfield"));
    }
}
