package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import projects.com.communication.tool.components.CampaignComponent;
import settings.SQLConnector;

import static common.ConciseApi.sleep;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class CampaignStep {

    private CampaignComponent component;

    public CampaignStep(WebDriver driver) {
        this.component = new CampaignComponent(driver);
    }

    @Step("Open campaign page")
    public void openCampaignPage() {
        component.open(getProperty("communication.tool.url") + "campaigns/");
    }

    @Step("Create new campaign")
    public void createCampaign(String campaignName, String companyName) {
        component.getCreateCampaignButton().click();
        component.getNameInput().sendKeys(campaignName);
        component.actionClick(component.getCompanySelect());
        component.getCompanyInput().sendKeys(companyName, Keys.ENTER);
        component.getSubmitButton().click();
    }

    @Step("Select campaign in list")
    public void selectCampaignInList(String campaignName) {
        WebElement campaignItem = component.getElementInListByText(campaignName, component.getCampaignInList());
        campaignItem.click();
        component.waitForText(component.getCampaignNameInPreview(), campaignName);
    }

    @Step("Activate communication")
    public void activateCommunication() {
        component.assertThat(elementToBeClickable(component.getStartCommunicationButton()));
        component.getStartCommunicationButton().click();
        component.waitForPartOfText(component.getSendingStatus(), "In progress");
    }

    @Step("Verify that company is displayed in list")
    public boolean isCompanyDisplayedInList(String companyName) {
        sleep(2000);
        component.isElementPresent(component.getCompanyInList());
        return component.isTextDisplayed(companyName, component.getCompanyInList());
    }

    @Step("Verify that campaign is displayed in list")
    public boolean isCampaignDisplayedInList(String campaignName) {
        component.isElementPresent(component.getCampaignInList());
        return component.isTextDisplayed(campaignName, component.getCampaignInList());
    }

    @Step("Verify that campaign is assign to company")
    public boolean isCampaignAssignToCompany(String companyName, String campaignName) {
        LOG.info("Get company id in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT Id FROM CommunicationTool.dbo.Company WHERE Name = '" + companyName + "'";
        String companyId = connector.getStringValueInDB(query, "Id");
        LOG.info("Get campaign name in the database");
        String queryCampaign = "SELECT Name FROM CommunicationTool.dbo.Campaign WHERE CompanyId = '" + companyId + "'";
        String campaignNameInDb = connector.getStringValueInDB(queryCampaign, "Name");
        connector.closeConnection();
        return campaignNameInDb.equals(campaignName);
    }

    @Step("Create campaign in the database")
    public void createCampaignInDB(String campaignName, String companyName) {
        LOG.info("Create campaign in the database");
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
                ",'FLE Standard Time', 124, 'body', 'subject', 1);";
        connector.executeQuery(query);
        LOG.info("Successfully created");
        connector.closeConnection();
    }
}
