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

    public WebElement getAddButton(){
        return $(cssSelector(".gateways__add-new > button"));
    }

    public WebElement getFromNameInput(){
        return $(cssSelector(".card--active__content .control:nth-of-type(1) .control__placeholder input"));
    }

    public WebElement getFromEmailInput(){
        return $(cssSelector(".card--active__content .control:nth-of-type(2) .control__placeholder input"));
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
        return $(cssSelector(".v-select__slot [placeholder=\"Select or type a placeholder\"]"));
    }

    public WebElement getPlaceholderValueInput(){
        return $(cssSelector(".card--active__content .control:nth-of-type(3) .control__placeholder input"));
    }

    public By getFromNameValue(){
        return by(className("card__textfield"));
    }

    public WebElement getEditRepsButton(){
        return $(cssSelector(".gateways__card .v-btn__content"));
    }

    public WebElement getDeleteRepsButton(){
        return $(cssSelector(".btn-delete"));
    }

    public By getRepsActions(){
        return by(className("menu-popup-link"));
    }
}
