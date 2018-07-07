package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.ContactsComponent;
import settings.SQLConnector;

import java.util.UUID;

import static java.util.UUID.randomUUID;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static projects.com.communication.tool.common.CommStackDB.*;
import static settings.SQLConnector.*;

public class ContactsStep {

    private ContactsComponent component;
    public static final int WORK_EMAIL_TYPE = 4098;

    public ContactsStep(WebDriver driver) {
        this.component = new ContactsComponent(driver);
    }

    @Step("Open contacts tab")
    public void openContactsTab() {
        component.scrollUp(component.getContactsTab());
        component.getContactsTab().click();
        component.getExpansionPanel().click();
    }

    @Step("Open 'Add filters' pop up")
    public void openAddFiltersPopUp() {
        component.clickToElement(component.getOpenPopUpButton());
    }

    @Step("Open 'Edit filters' pop up")
    public void openEditFiltersPopUp() {
        component.clickToElement(component.getEditFilterButton());
    }

    @Step("Delete filter")
    public void deleteFilter() {
        component.clickToElement(component.getDeleteFilterButton());
    }

    @Step("Add filter to campaign")
    public void addFilterToCampaign() {
        component.clickToElement(component.getAddToCampaignButton());
        component.assertThat(attributeContains(component.getAddToCampaignButton(), "disabled", "true"));
        component.assertThat(elementToBeClickable(component.getEditFilterButton()));
    }

    @Step("Verify that filters are displayed in the filter's table")
    public boolean isFilterDisplayedInTable(String value, int count) {
        return component.isTextDisplayed(value, component.getAppliedFilterInTable()) &
                component.isTextDisplayed(String.valueOf(count), component.getAppliedFilterInTable());
    }

    @Step("Insert contact to the database")
    public void insertContactToDb(String firstName, String email, int type) {
        SQLConnector connector = new SQLConnector();
        UUID contactId = randomUUID();
        String query = INSERT_INTO + CONTACT_DB + "(Id, FirstName, IsVerifiedLocation)" +
                VALUES + "('" + contactId + "', '" + firstName + "', 1);" +
                INSERT_INTO + CONTACT_INFO_DB + "(Id, ContactId, IsVerified, Value, [Type]) " +
                VALUES + "('" + randomUUID() + "', '" + contactId + "', 'false', '" + email + "', '" + type + "');";
        connector.executeQuery(query);
    }

    @Step("Insert contact to the database")
    public void insertContactToDb(String firstName) {
        SQLConnector connector = new SQLConnector();
        String query = INSERT_INTO + CONTACT_DB + "(Id, FirstName, IsVerifiedLocation)" +
                VALUES + "('" + randomUUID() + "', '" + firstName + "', 1);";
        connector.executeQuery(query);
    }

    @Step("Delete contact from the database")
    public void deleteContactFromDb(String firstName) {
        SQLConnector connector = new SQLConnector();
        connector.executeQuery(DELETE_FROM + CONTACT_DB + WHERE + "FirstName='" + firstName + "';");
    }

    @Step("Delete contact from the database")
    public void deleteContactFromDb(String firstName, String email) {
        SQLConnector connector = new SQLConnector();
        String query = DELETE_FROM + MESSAGE_DB + ";" + DELETE_FROM + CONTACT_INFO_DB + WHERE + "Value='" + email + "';" +
                DELETE_FROM + CONTACT_DB + WHERE + "FirstName='" + firstName + "';";
        connector.executeQuery(query);
    }

    @Step("Delete all campaign's filter in the database")
    public void deleteAllFiltersInCampaignDb() {
        SQLConnector connector = new SQLConnector();
        connector.executeQuery(DELETE_FROM + CAMPAIGN_FILTER_DB);
    }

    @Step("Insert campaign's filter to the database")
    public void insertFilterToCampaignDb(String campaignName, String firstName) {
        SQLConnector connector = new SQLConnector();
        String campaignId = connector.getValueInDb(SELECT_FROM + CAMPAIGN_DB + " WHERE Name = '" + campaignName + "'", "Id");
        String query = INSERT_INTO + CAMPAIGN_FILTER_DB + VALUES +
                "('" + randomUUID() + "', {ts '2018-06-15 12:12:26.738'}, '" + campaignId + "'" + ", 1, '[{\"ModelFullName\":" +
                "\"CommunicationTool.Data.Filters.Entities.Contact\",\"Column\":\"FirstName\",\"Type\":1," +
                "\"Params\":[\"" + firstName + "\"]}]');";
        connector.executeQuery(query);
    }
}
