package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class TemplateComponent extends BaseComponent {

    public TemplateComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getSubjectInput(){
        return $(name("subject"));
    }

    public WebElement getBodyInput(){
        return $(id("tinymce"));
    }

    public By getPlaceholderButton(){
        return by(cssSelector(".control button.btn-tag"));
    }

    public WebElement getContactInfoInput(){
        return $(id("contactInfo"));
    }

    public WebElement getTestingEmailInput(){
        return $(id("email"));
    }

    public WebElement getSendButton(){
        return $(cssSelector(".test-template .btn-save"));
    }

    public By getTestStatus(){
        return by(className("test-status"));
    }
}
