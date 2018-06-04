package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.ContactsComponent;
import projects.com.communication.tool.steps.contacts.FiltersStep;
import settings.SQLConnector;

import static common.ConciseApi.sleep;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static projects.com.communication.tool.steps.contacts.FiltersStep.*;
import static settings.SQLConnector.EQUAL;
import static settings.SeleniumListener.LOG;

public class ContactsStep {

    private ContactsComponent component;
    private FiltersStep filtersStep;
    public static final int WORK_EMAIL_TYPE = 4098;
    public static final int PERSONAL_EMAIL_TYPE = 4112;
    public static final int ALTERNATE_EMAIL_TYPE = 4128;

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

    @Step("Insert contact to the database")
    public void insertContactToDb(String firstName, String email, int type) {
        LOG.info("Insert contact to the database with first name: " + firstName);
        SQLConnector connector = new SQLConnector();
        String query = "DECLARE @Id uniqueidentifier SET @Id = NEWID() " +
                "INSERT INTO CommunicationTool.dbo.Contact (Id, FirstName, IsVerifiedLocation) " +
                "VALUES(@Id, '" + firstName + "', 1);" +
                "DECLARE @InfoId uniqueidentifier SET @InfoId = NEWID() " +
                "INSERT INTO CommunicationTool.dbo.ContactInfo (Id, ContactId, IsVerified, Value, [Type]) " +
                "VALUES(@InfoId, @Id, 'false', '" + email + "', '" + type + "');";
        connector.executeQuery(query);
        LOG.info("Successfully inserted");
    }

    @Step("Delete contact from the database")
    public void deleteContactFromDb(String firstName, String email) {
        LOG.info("Delete contact from the database with first name and email : " + firstName + ", " + email);
        SQLConnector connector = new SQLConnector();
        String query = "DELETE FROM CommunicationTool.dbo.ContactInfo WHERE Value='" + email + "';" +
                "DELETE FROM CommunicationTool.dbo.Contact WHERE FirstName='" + firstName + "';";
        connector.executeQuery(query);
        LOG.info("Successfully deleted");
    }

    @Step("Insert contact to the database")
    public void insertContactToDb(String firstName) {
        LOG.info("Insert contact to the database with first name: " + firstName);
        SQLConnector connector = new SQLConnector();
        String query = "DECLARE @Id uniqueidentifier SET @Id = NEWID() " +
                "INSERT INTO CommunicationTool.dbo.Contact (Id, FirstName, IsVerifiedLocation) " +
                "VALUES(@Id, '" + firstName + "', 1);";
        connector.executeQuery(query);
        LOG.info("Successfully inserted");
    }

    @Step("Delete contact from the database")
    public void deleteContactFromDb(String firstName) {
        LOG.info("Delete contact from the database with first name: " + firstName);
        SQLConnector connector = new SQLConnector();
        String query = "DELETE FROM CommunicationTool.dbo.Contact WHERE FirstName='" + firstName + "';";
        connector.executeQuery(query);
        LOG.info("Successfully deleted");
    }
}
