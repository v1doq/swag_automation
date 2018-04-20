package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.ContactsComponent;

import static common.ConciseApi.sleep;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static projects.com.communication.tool.steps.FiltersStep.EQUAL_CRITERION;
import static projects.com.communication.tool.steps.FiltersStep.POSITION_FILTER;
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
        component.assertThat(attributeContains(component.getAddContactButton(), "disabled", "true"));
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
        filtersStep.applyAllFilters(POSITION_FILTER, EQUAL_CRITERION, value);
        int count = filtersStep.getValueByCriterion("[Position]", EQUAL, value);
        filtersStep.waitForRecordsResult(count);
        saveContactsInCampaign();
        return count;
    }
}
