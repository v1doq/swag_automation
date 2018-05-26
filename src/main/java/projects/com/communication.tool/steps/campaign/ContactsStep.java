package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.ContactsComponent;
import projects.com.communication.tool.steps.contacts.FiltersStep;

import static common.ConciseApi.sleep;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static projects.com.communication.tool.steps.contacts.FiltersStep.*;
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
        component.scrollUp();
        component.assertThat(elementToBeClickable(component.getContactsTab()));
        component.getContactsTab().click();
    }

    @Step("Open 'Add contacts' pop up")
    public void openContactsPopUp() {
        component.assertThat(elementToBeClickable(component.getOpenPopUpButton()));
        component.getOpenPopUpButton().click();
    }

    @Step("Add contacts to campaign")
    public int addContactToCampaign(String firstName) {
        openContactsPopUp();
        filtersStep.applyAllFilters(ENTITY_USER, FIRST_NAME_FILTER, EQUAL_CRITERION, firstName);
        int count = filtersStep.getValueByCriterion("FirstName", EQUAL, firstName);
        filtersStep.waitForRecordsResult(count);
        saveContactsInCampaign();
        return count;
    }

    @Step("Add contacts to campaign")
    public void saveContactsInCampaign() {
        component.getAddContactButton().click();
        component.assertThat(attributeContains(component.getAddContactButton(), "disabled", "true"));
        sleep(3000);
    }

    @Step("Verify that contacts are displayed in the contact's table")
    public boolean isContactsAddedToCampaign(String value) {
        return component.isTextDisplayed(value, component.getContactsTable());
    }

    @Step("Get list of contacts by text")
    public int getContactsListSizeByText(String text) {
        By locator = component.getContactsTable();
        component.waitForText(locator, text);
        return component.getListSizeByText(locator, text);
    }
}
