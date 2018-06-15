package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.CampaignFiltersComponent;
import projects.com.communication.tool.steps.contacts.FiltersStep;
import settings.SQLConnector;

import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static projects.com.communication.tool.steps.contacts.FiltersStep.*;
import static settings.SQLConnector.EQUAL;
import static settings.SeleniumListener.LOG;

public class CampaignFiltersStep {

    private CampaignFiltersComponent component;
    private FiltersStep filtersStep;
    public static final int WORK_EMAIL_TYPE = 4098;
    public static final int PERSONAL_EMAIL_TYPE = 4112;
    public static final int ALTERNATE_EMAIL_TYPE = 4128;

    public CampaignFiltersStep(WebDriver driver) {
        this.component = new CampaignFiltersComponent(driver);
        this.filtersStep = new FiltersStep(driver);
    }

    @Step("Open filters tab")
    public void openFiltersTab() {
        component.scrollUp();
        component.assertThat(elementToBeClickable(component.getFiltersTab()));
        component.getFiltersTab().click();
    }

    @Step("Add filter to campaign")
    public int addFilterToCampaign(String firstName) {
        component.assertThat(elementToBeClickable(component.getOpenPopUpButton()));
        component.getOpenPopUpButton().click();
        filtersStep.applyAllFilters(ENTITY_USER, FIRST_NAME_FILTER, EQUAL_CRITERION, firstName);
        int count = filtersStep.getValueByCriterion("FirstName", EQUAL, firstName);
        filtersStep.waitForRecordsResult(count);
        component.getAddToCampaignButton().click();
        component.assertThat(attributeContains(component.getAddToCampaignButton(), "disabled", "true"));
        return count;
    }

    @Step("Verify that filters are displayed in the filter's table")
    public boolean isFilterDisplayedInTable(String value, int count) {
        boolean isDisplayed = false;
        if (component.isTextDisplayed(value, component.getAppliedFilterInTable())){
            isDisplayed = true;
        } else if (component.isTextDisplayed(String.valueOf(count), component.getAppliedFilterInTable())){
            isDisplayed = true;
        }
        return isDisplayed;
    }

    @Step("Get list of contacts by text")
    public int getContactsListSizeByText(String text) {
        By locator = component.getAppliedFilterInTable();
        component.waitForText(locator, text);
        return component.getListSizeByText(locator, text);
    }

    @Step("Insert contact to the database")
    public void insertContactToDb(String firstName, String email, int type) {
        LOG.info("Insert contact to the database with first name and email: " + firstName + ", " + email);
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
        String query = "USE CommunicationTool DELETE FROM Message; DELETE FROM ContactInfo WHERE Value='" + email + "';" +
                "DELETE FROM Contact WHERE FirstName='" + firstName + "';";
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
