package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.ContactsComponent;

import static common.ConciseApi.sleep;
import static projects.com.communication.tool.steps.FiltersStep.EQUAL_CRITERION;
import static projects.com.communication.tool.steps.FiltersStep.STREET;
import static settings.SQLConnector.EQUAL;

public class ContactsStep {

    private ContactsComponent component;
    private FiltersStep filtersStep;

    public ContactsStep(WebDriver driver) {
        this.component = new ContactsComponent(driver);
        this.filtersStep = new FiltersStep(driver);
    }

    @Step("Open contacts tab")
    public void openContactsTab() {
        component.getContactsTab().click();
    }

    @Step("Open 'Add contacts' pop up")
    public void openContactsPopUp() {
        component.getOpenPopUpButton().click();
    }

    @Step("Add contacts to campaign")
    public void saveContactsInCampaign() {
        component.getAddContactButton().click();
        sleep(1000);
    }

    @Step("Verify that contacts are displayed in the contact's table")
    public boolean isContactsAddedToCampaign(String value) {
        By locator = component.getContactsTable();
        component.waitForText(locator, value);
        return component.isTextDisplayed(value, locator);
    }

    @Step("Add contacts to campaign")
    public int addContactToCampaign(String value) {
        openContactsPopUp();
        filtersStep.applyAllFilters(STREET, EQUAL_CRITERION, value);
        int count = filtersStep.getValueByCriterion(STREET, EQUAL, value);
        filtersStep.waitForRecordsResult(count);
        saveContactsInCampaign();
        return count;
    }
}
