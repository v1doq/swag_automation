package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.ContactsComponent;

public class ContactsStep {

    private ContactsComponent component;

    public ContactsStep(WebDriver driver) {
        this.component = new ContactsComponent(driver);
    }

    @Step("Open contacts tab")
    public void openContactsTab() {
        component.getContactsTab().click();
    }

    @Step("Open 'Add contacts' pop up")
    public void selectContacts() {
        component.getAddContactsButton().click();
    }

    @Step("Add contacts to campaign")
    public void addContactsToCampaign() {
        component.getSaveContactsButton().click();
    }

    @Step("Add contacts to campaign")
    public boolean isContactsAddedToCampaign(String value) {
        By locator = component.getContactsTable();
        component.waitForText(locator, value);
        return component.isTextDisplayed(value, locator);
    }
}
