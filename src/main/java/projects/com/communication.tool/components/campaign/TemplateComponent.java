package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class TemplateComponent extends BaseComponent {

    public TemplateComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getTemplateTab(){
        return $(cssSelector("div.tabs> ul>li:nth-child(2)"));
    }

    public WebElement getSubjectInput(){
        return $(name("subject"));
    }

    public WebElement getBodyInput(){
        return $(cssSelector(".ql-editor"));
    }

    public WebElement getSaveButton(){
        return $(cssSelector(".is-active > div > button"));
    }

    public By getPlaceholderButton(){
        return by(cssSelector(".btn-tag--representative"));
    }
}
