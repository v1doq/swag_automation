package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import projects.com.communication.tool.components.CampaignComponent;
import settings.SQLConnector;

import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
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
        component.assertThat(invisibilityOf(component.getSubmitButton()));
    }

    @Step("Select campaign in list")
    public void selectCampaignInList(String campaignName) {
        WebElement element = component.getElementInList(campaignName, component.getCampaignItemsInList());
        element.click();
        component.waitForText(component.getCampaignNameInPreview(), campaignName);
    }

    @Step("Verify that company is displayed in list")
    public boolean isCompanyDisplayedInList(String companyName) {
        component.assertThat(elementToBeClickable(component.getCampaignItemsInList()));
        return component.isTextDisplayed(companyName, tagName("h3"));
    }

    @Step("Verify that campaign is displayed in list")
    public boolean isCampaignDisplayedInList(String campaignName) {
        return component.isTextDisplayed(campaignName, tagName("li"));
    }

    @Step("Verify that campaign is assign to company")
    public boolean isCampaignAssignToCompany(String companyName, String campaignName) {
        LOG.info("Get company id in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT Id FROM CommunicationTool.dbo.Client WHERE Name = '" + companyName + "'";
        String companyId = connector.getStringValueInDB(query, "Id");
        LOG.info("Get campaign name in the database");
        String queryCampaign = "SELECT Name FROM CommunicationTool.dbo.Campaign WHERE ClientId = '" + companyId + "'";
        String campaignNameInDb = connector.getStringValueInDB(queryCampaign, "Name");
        connector.closeConnection();
        return campaignNameInDb.equals(campaignName);
    }

    @Step("Delete company in the database")
    public void deleteCompanyInDB(String companyName, String campaignName) {
        LOG.info("Get campaign id in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT Id FROM CommunicationTool.dbo.Campaign WHERE Name = '" + campaignName + "'";
        String id = connector.getStringValueInDB(query, "Id");
        LOG.info("Delete email communication in the database where CampaignId = '" + id + "'");
        connector.executeQuery("DELETE FROM CommunicationTool.dbo.EmailCommunication WHERE CampaignId = '" + id + "'");
        LOG.info("Successfully deleted");
        LOG.info("Delete company in the database where company name = '" + companyName + "'");
        connector.executeQuery("DELETE FROM CommunicationTool.dbo.Client WHERE Name = '" + companyName + "'");
        LOG.info("Successfully deleted");
        connector.closeConnection();
    }

    @Step("Create campaign in the database")
    public void createCampaignInDB(String campaignName, String companyName) {
        LOG.info("Create campaign in the database");
        SQLConnector connector = new SQLConnector();
        String query = "DECLARE @clientId uniqueidentifier SET @clientId = NEWID() " +
                "DECLARE @campaignId uniqueidentifier SET @campaignId = NEWID() " +
                "DECLARE @communicationId uniqueidentifier SET @communicationId = NEWID() " +
                "INSERT INTO CommunicationTool.dbo.Client (Id, Name) VALUES(@clientId, '" + companyName + "'); " +
                "INSERT INTO CommunicationTool.dbo.Campaign (Id,ClientId,Name) " +
                "VALUES(@campaignId, @clientId, '" + campaignName + "'); " +
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
