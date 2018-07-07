package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.CampaignComponent;
import settings.SQLConnector;

import java.util.List;

import static java.util.Arrays.asList;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class CampaignStep {

    private CampaignComponent component;
    public static final byte MIN_COMPANY_NAME_LENGTH = 5;
    public static final byte MIN_CAMPAIGN_NAME_LENGTH = 5;
    public static final byte MIN_CAMPAIGN_DESC_LENGTH = 1;
    public static final String DUPLICATE_CAMPAIGN_ERROR = "Campaign with this name already exists";
    public static final String COMMUNICATION_START_ERROR = "Could not start the campaign";

    public CampaignStep(WebDriver driver) {
        this.component = new CampaignComponent(driver);
    }

    @Step("Open campaign page")
    public void openCampaignPage() {
        component.open(getProperty("communication.tool.url") + "campaigns/");
    }

    @Step("Create campaign")
    public void createCampaign(String campaignName, String companyName) {
        component.getCreateCampaignButton().click();
        component.getCompanyNameInput().sendKeys(companyName, Keys.ENTER);
        component.getCampaignNameInput().sendKeys(campaignName);
        component.getModalTitle().click();
        component.actionClick(component.getSubmitButton());
    }

    @Step("Select campaign in list")
    public void selectCampaignInList(String campaignName) {
        searchInCampaignList(campaignName);
        component.getElementInListByText(campaignName, component.getCampaignInList()).click();
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

    @Step("Update campaign's name")
    public void updateCampaignName(String campaignName) {
        component.getEditCampaignNameButton().click();
        component.clearAndSendKeys(component.getUpdateCampaignNameInput(), campaignName);
        component.getUpdateCampaignNameButton().click();
        component.waitForText(component.getCampaignNameInPreview(), campaignName);
    }

    @Step("Update campaign's desc")
    public void updateCampaignDesc(String campaignDesc) {
        component.getEditCampaignDescButton().click();
        component.clearAndSendKeys(component.getUpdateCampaignDescInput(), campaignDesc);
        component.getUpdateCampaignDescButton().click();
        while (component.getCampaignNameInPreviewElement().getText().contains("description")){
            component.waitForText(component.getCampaignDescInPreview(), campaignDesc);
        }
    }

    @Step("Delete campaign")
    public void deleteCampaign() {
        component.getDeleteCampaignPreview().click();
        component.getDeleteCampaignButton().click();
        component.getConfirmDeletionButton().click();
        component.assertThat(invisibilityOf(component.getConfirmDeletionButton()));
    }

    public String getCampaignNameInPreview() {
        return component.getCampaignNameInPreviewElement().getText();
    }

    public String getCampaignDescInPreview() {
        return component.getCampaignDescInPreviewElement().getText();
    }

    @Step("Activate communication")
    public void activateCommunication() {
        component.scrollUp(component.getCommunicationButton());
        component.getCommunicationButton().click();
    }

    @Step("Verify that communication successfully started")
    public boolean isCommunicationStarted() {
        try {
            component.waitForText(component.getSendingStatus(), "In progress");
        } catch (TimeoutException e){
            return false;
        }
        return true;
    }

    @Step("Stop communication")
    public void stopCommunication() {
        component.scrollUp(component.getCommunicationButton());
        component.getCommunicationButton().click();
        component.waitForText(component.getSendingStatus(), "Paused");
    }

    @Step("Verify that company and campaign are displayed in list")
    public boolean isCompanyAndCampaignInList(String companyName, String campaignName) {
        component.assertThat(attributeToBe(className("modal-mask"), "style", "display: none;"));
        searchInCampaignList(companyName);
        try {
            component.waitForText(component.getCompanyInList(), companyName);
        } catch (TimeoutException e){
            return false;
        }
        return component.isTextDisplayed(campaignName, component.getCampaignInList());
    }

    @Step("Is server error message displayed")
    public boolean isServerErrorDisplayed(String error) {
        try {
            component.waitForText(component.getServerError(), error);
        } catch (TimeoutException e){
            return false;
        }
        return true;
    }

    @Step("Is error validation messages are displayed")
    public boolean isValidationMessagesDisplayed(){
        boolean isDisplayed = false;
        for (String error : REQUIRED_FIELDS_ERRORS) {
            isDisplayed = component.isTextDisplayed(error, tagName("div"));
            if (!isDisplayed){
                break;
            }
        }
        return isDisplayed;
    }

    private static final List<String> REQUIRED_FIELDS_ERRORS = asList(
            "The name field is required.",
            "The company field is required."
    );

    private void searchInCampaignList(String value) {
        component.clearAndSendKeys(component.getSearchInput(), value);
    }

    @Step("Verify that campaign is assign to company")
    public String getCampaignNameByCompanyName(String companyName, String campaignName) {
        LOG.info("Get company id in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT Id FROM CommunicationTool.dbo.Company WHERE Name = '" + companyName + "'";
        String companyId = connector.getStringValueInDB(query, "Id");
        LOG.info("Get campaign name in the database");
        String queryCampaign = "SELECT Name FROM CommunicationTool.dbo.Campaign WHERE CompanyId = '" + companyId + "'" +
                "AND Name ='" + campaignName + "'";
        return connector.getStringValueInDB(queryCampaign, "Name");
    }

    @Step("Create campaign in the database")
    public void createCampaignInDB(String campaignName, String companyName) {
        LOG.info("Create campaign in the database with name: " + campaignName);
        SQLConnector connector = new SQLConnector();
        String query = "DECLARE @companyId uniqueidentifier SET @companyId = NEWID() " +
                "DECLARE @campaignId uniqueidentifier SET @campaignId = NEWID() " +
                "DECLARE @communicationId uniqueidentifier SET @communicationId = NEWID() " +
                "INSERT INTO CommunicationTool.dbo.Company (Id, Name) VALUES(@companyId, '" + companyName + "'); " +
                "INSERT INTO CommunicationTool.dbo.Campaign (Id, CompanyId, Name, Schedule_EndTime, Schedule_Interval, " +
                "Schedule_StartTime, Schedule_TimeZone, Schedule_WeekDays, CreatedAt, RootFlowId, Status)\n" +
                "VALUES(@campaignId, @companyId, '" + campaignName + "', '23:00:00.000', 30, '06:00:00.000', " +
                "'FLE Standard Time', 254, {ts '2018-06-13 09:04:06.854'}, NULL, 1);";
        connector.executeQuery(query);
        LOG.info("Successfully created");
    }
}
