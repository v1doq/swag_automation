package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.CampaignComponent;
import settings.SQLConnector;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class CampaignStep {

    private CampaignComponent component;
    public static final byte MIN_CAMPAIGN_NAME_LENGTH = 5;
    public static final byte MIN_CAMPAIGN_DESC_LENGTH = 1;
    public static final byte MIN_COMPANY_NAME_LENGTH = 5;

    public CampaignStep(WebDriver driver) {
        this.component = new CampaignComponent(driver);
    }

    @Step("Open campaign page")
    public void openCampaignPage() {
        component.open(getProperty("communication.tool.url") + "campaigns/");
    }

    @Step("Create campaign")
    public void createCampaign(String campaignName, String companyName) {
        openCampaignPopUp();
        component.getCompanyNameInput().sendKeys(companyName, Keys.ENTER);
        component.getCampaignNameInput().sendKeys(campaignName);
        component.getModalTitle().click();
        submitCampaign();
    }

    @Step("Select campaign in list")
    public void selectCampaignInList(String campaignName) {
        searchInCampaignList(campaignName);
        component.clickToElementInListByText(campaignName, component.getCampaignInList());
        try {
            component.waitForText(component.getCampaignNameInPreview(), campaignName);
        } catch (TimeoutException e){
            while (!component.getCampaignNameInPreviewElement().getText().equals(campaignName)){
                component.getDriver().navigate().refresh();
                selectCampaignInList(campaignName);
                if (component.getCampaignNameInPreviewElement().getText().equals(campaignName)){
                    break;
                }
            }
        }
    }

    @Step("Open campaign pop up")
    public void openCampaignPopUp() {
        component.getCreateCampaignButton().click();
    }

    @Step("Save campaign")
    public void submitCampaign() {
        component.getSubmitButton().click();
    }

    @Step("Activate communication")
    public void activateCommunication() {
        component.scrollUp();
        component.assertThat(elementToBeClickable(component.getStartCommunicationButton()));
        component.getStartCommunicationButton().click();
        component.waitForPartOfText(component.getSendingStatus(), "In progress");
    }

    @Step("Update campaign's name and description")
    public void updateCampaign(String campaignName, String campaignDesc) {
        updateCampaignName(campaignName);
        updateCampaignDesc(campaignDesc);
    }

    @Step("Update campaign's name")
    public void updateCampaignName(String campaignName) {
        component.getEditCampaignNameButton().click();
        component.clearAndSendKeys(component.getUpdateCampaignNameInput(), campaignName);
        component.getUpdateCampaignNameButton().click();
        component.waitForText(component.getCampaignNameInPreview(), campaignName);
    }

    @Step("Update campaign's desc")
    private void updateCampaignDesc(String campaignDesc) {
        component.getEditCampaignDescButton().click();
        component.clearAndSendKeys(component.getUpdateCampaignDescInput(), campaignDesc);
        component.getUpdateCampaignDescButton().click();
        component.waitForText(component.getCampaignDescInPreview(), campaignDesc);
    }

    public String getCampaignNameInPreview() {
        return component.getEditCampaignNameButton().getText();
    }

    public String getCampaignDescInPreview() {
        return component.getEditCampaignDescButton().getText();
    }

    @Step("Verify that company and campaign are displayed in list")
    public boolean isCompanyAndCampaignInList(String companyName, String campaignName) {
        searchInCampaignList(companyName);
        component.waitForText(component.getCompanyInList(), companyName);
        boolean isDisplayed = false;
        if (component.isTextDisplayed(companyName, component.getCompanyInList())) {
            isDisplayed = true;
        }
        if (component.isTextDisplayed(campaignName, component.getCampaignInList())) {
            isDisplayed = true;
        }
        return isDisplayed;
    }

    private void searchInCampaignList(String value) {
        component.getSearchInput().sendKeys(value);
    }

    @Step("Is error message displayed for duplicate campaign name")
    public boolean isCampaignServerErrorDisplayedInPopUp() {
        String message = "Campaign with this name already exists";
        By locator = component.getServerErrorInPopUp();
        component.waitForText(locator, message);
        return component.isTextDisplayed(message, locator);
    }

    @Step("Is error message displayed for duplicate campaign name")
    public boolean isCampaignServerErrorDisplayedInPreview() {
        String message = "Campaign with this name already exists";
        By locator = component.getServerErrorInPreview();
        component.waitForText(locator, message);
        return component.isTextDisplayed(message, locator);
    }

    @Step("Verify that error message is displayed for campaign field")
    public boolean isCampaignErrorDisplayed() {
        component.assertThat(visibilityOfElementLocated(component.getCampaignNameError()));
        return component.isTextDisplayed("The name field is required.", component.getCampaignNameError());
    }

    @Step("Verify that error message is displayed for company field")
    public boolean isCompanyErrorDisplayed() {
        component.assertThat(visibilityOfElementLocated(component.getCompanyNameError()));
        return component.isTextDisplayed("The company field is required.", component.getCompanyNameError());
    }

    @Step("Verify that campaign is assign to company")
    public boolean isCampaignAssignToCompany(String companyName, String campaignName) {
        LOG.info("Get company id in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT Id FROM CommunicationTool.dbo.Company WHERE Name = '" + companyName + "'";
        String companyId = connector.getStringValueInDB(query, "Id");
        LOG.info("Get campaign name in the database");
        String queryCampaign = "SELECT Name FROM CommunicationTool.dbo.Campaign WHERE CompanyId = '" + companyId + "'" +
                "AND Name ='" + campaignName + "'";
        String campaignNameInDb = connector.getStringValueInDB(queryCampaign, "Name");
        connector.closeConnection();
        return campaignNameInDb.equals(campaignName);
    }

    @Step("Create campaign in the database")
    public void createCampaignInDB(String campaignName, String companyName) {
        LOG.info("Create campaign in the database with name: " + campaignName);
        SQLConnector connector = new SQLConnector();
        String query = "DECLARE @companyId uniqueidentifier SET @companyId = NEWID() " +
                "DECLARE @campaignId uniqueidentifier SET @campaignId = NEWID() " +
                "DECLARE @communicationId uniqueidentifier SET @communicationId = NEWID() " +
                "INSERT INTO CommunicationTool.dbo.Company (Id, Name) VALUES(@companyId, '" + companyName + "'); " +
                "INSERT INTO CommunicationTool.dbo.Campaign (Id, CompanyId, Name) " +
                "VALUES(@campaignId, @companyId, '" + campaignName + "'); " +
                "INSERT INTO CommunicationTool.dbo.EmailCommunication " +
                "(Id, CampaignId, CreatedAt, Schedule_EndTime, Schedule_Interval, Schedule_StartTime, " +
                "Schedule_TimeZone, Schedule_WeekDays, Template_Body, Template_Subject, Status) " +
                "VALUES(@communicationId, @campaignId, {ts '2018-04-02 10:14:33.019'},'17:00:00.000', 2,'08:00:00.000'" +
                ",'FLE Standard Time', 124, '', '', 1);";
        connector.executeQuery(query);
        LOG.info("Successfully created");
        connector.closeConnection();
    }
}
