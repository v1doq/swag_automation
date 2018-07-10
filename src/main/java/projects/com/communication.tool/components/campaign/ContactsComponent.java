package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.linkText;

public class ContactsComponent extends BaseComponent {

    public ContactsComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getContactsTab(){
        return $(linkText("Contacts"));
    }

    public WebElement getExpansionPanel(){
        return $(className("v-expansion-panel"));
    }

    public WebElement getOpenPopUpButton(){
        return $(cssSelector(".v-expansion-panel .btn-add"));
    }

    public WebElement getAddToCampaignButton(){
        return $(cssSelector(".modal-container--large .btn-save"));
    }

    public WebElement getEditFilterButton(){
        return $(cssSelector(".contacts-table__actions .btn-edit"));
    }

    public WebElement getDeleteFilterButton(){
        return $(cssSelector(".contacts-table__actions .btn-delete-icon"));
    }

    public By getAppliedFilterInTable(){
        return by(cssSelector(".contacts-table td"));
    }
}
