package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class RepresentativeComponent extends BaseComponent {

    public RepresentativeComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getRepresentativeTab(){
        return $(cssSelector("div.tabs> ul>li:nth-child(2)"));
    }

    public WebElement getAddButton(){
        return $(cssSelector(".gateways__add-new > button"));
    }

    public WebElement getFromNameInput(){
        return $(cssSelector(".control__placeholder > div > div.input-group__input > input[type=\"text\"]"));
    }

    public WebElement getFromEmailInput(){
        return $(cssSelector("div:nth-child(2)>div.control__placeholder>div>div.input-group__input>input[type=\"text\"]"));
    }

    public WebElement getSmtpHostInput(){
        return $(name("smtpHost"));
    }

    public WebElement getImapHostInput(){
        return $(name("imapHost"));
    }

    public WebElement getLoginInput(){
        return $(name("login"));
    }

    public WebElement getPasswordInput(){
        return $(name("password"));
    }

    public WebElement getRepsTitle(){
        return $(className("card__title"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector(".btn-save.btn-medium"));
    }

    public WebElement getNewPlaceholderButton(){
        return $(cssSelector("div:nth-child(3) > button > div"));
    }

    public WebElement getPlaceholderKeyInput(){
        return $(cssSelector("div.input-group__selections > input"));
    }

    public WebElement getPlaceholderValueInput(){
        return $(cssSelector("div:nth-child(3)>div.control__placeholder>div>div.input-group__input>input[type=\"text\"]"));
    }

    public By getFromNameValue(){
        return by(cssSelector(".card__textfield"));
    }
}
