package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.CampaignFiltersComponent;
import settings.SQLConnector;

import static org.openqa.selenium.support.ui.ExpectedConditions.attributeContains;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static settings.SeleniumListener.LOG;

public class CampaignFiltersStep {

    private CampaignFiltersComponent component;
    public static final int WORK_EMAIL_TYPE = 4098;
    public static final int PERSONAL_EMAIL_TYPE = 4112;
    public static final int ALTERNATE_EMAIL_TYPE = 4128;

    public CampaignFiltersStep(WebDriver driver) {
        this.component = new CampaignFiltersComponent(driver);
    }

    @Step("Open filters tab")
    public void openFiltersTab() {
        component.scrollUp(component.getFiltersTab());
        component.getFiltersTab().click();
    }

    @Step("Open 'Add filters' pop up")
    public void openAddFiltersPopUp() {
        component.assertThat(elementToBeClickable(component.getOpenPopUpButton()));
        component.getOpenPopUpButton().click();
    }

    @Step("Open 'Edit filters' pop up")
    public void openEditFiltersPopUp() {
        component.assertThat(elementToBeClickable(component.getEditFilterButton()));
        component.getEditFilterButton().click();
    }

    @Step("Delete filter")
    public void deleteFilter() {
        component.assertThat(elementToBeClickable(component.getDeleteFilterButton()));
        component.getDeleteFilterButton().click();
    }

    @Step("Add filter to campaign")
    public void addFilterToCampaign() {
        component.getAddToCampaignButton().click();
        component.assertThat(attributeContains(component.getAddToCampaignButton(), "disabled", "true"));
        component.assertThat(elementToBeClickable(component.getEditFilterButton()));
    }

    @Step("Verify that filters are displayed in the filter's table")
    public boolean isFilterDisplayedInTable(String value, int count) {
        return component.isTextDisplayed(value, component.getAppliedFilterInTable()) &
                component.isTextDisplayed(String.valueOf(count), component.getAppliedFilterInTable());
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

    @Step("Delete all campaign's filter in the database")
    public void deleteAllFiltersInCampaignDb() {
        LOG.info("Delete all campaign's filter in the database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM CommunicationTool.dbo.CampaignFilter");
    }

    @Step("Insert campaign's filter to the database")
    public void insertFilterToCampaignDb(String campaignName, String firstName) {
        LOG.info("Get campaign id in the database");
        SQLConnector connector = new SQLConnector();
        String campaignId = connector.getStringValueInDB("SELECT Id FROM CommunicationTool.dbo." +
                "Campaign WHERE Name = '" + campaignName + "'", "Id");
        LOG.info("Insert filter to campaign");
        String query = "DECLARE @Id uniqueidentifier SET @Id = NEWID()" +
                "INSERT INTO CommunicationTool.dbo.CampaignFilter" +
                "(Id, AppliedAt, CampaignId, Count, Query)" +
                "VALUES(@Id, {ts '2018-06-15 12:12:26.738'}, '" + campaignId + "', 1, '[{\"ModelFullName\":" +
                "\"CommunicationTool.Data.Filters.Entities.Contact\",\"Column\":\"FirstName\",\"Type\":1," +
                "\"Params\":[\"" + firstName + "\"]}]');";
        connector.executeQuery(query);
    }
}
