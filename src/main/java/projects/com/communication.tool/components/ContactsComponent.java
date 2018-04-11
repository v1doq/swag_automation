package projects.com.communication.tool.components;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;

public class ContactsComponent extends BaseComponent {

    public ContactsComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getContactsTab(){
        return $(cssSelector("div.tabs>ul>li:nth-child(4)"));
    }

    public WebElement getOpenPopUpButton(){
        return $(cssSelector("div.campaign-preview__contacts-header > div > button"));
    }

    public WebElement getAddContactButton(){
        return $(cssSelector(".is-horizontal>button"));
    }

    public By getContactsTable(){
        return by(cssSelector("td"));
    }
}
