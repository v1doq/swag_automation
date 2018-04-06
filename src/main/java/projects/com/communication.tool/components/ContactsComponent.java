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

    public WebElement getAddContactsButton(){
        return $(cssSelector(".btn-add"));
    }

    public WebElement getSaveContactsButton(){
        return $(cssSelector(".is-horizontal>button"));
    }

    public By getContactsTable(){
        return by(cssSelector("td"));
    }
}
