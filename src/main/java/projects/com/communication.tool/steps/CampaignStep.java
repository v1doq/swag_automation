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
        WebElement campaignItem = component.getElementInListByText(campaignName, component.getCampaignItemsInList());
        campaignItem.click();
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
        String query = "SELECT Id FROM CommunicationTool.dbo.Company WHERE Name = '" + companyName + "'";
        String companyId = connector.getStringValueInDB(query, "Id");
        LOG.info("Get campaign name in the database");
        String queryCampaign = "SELECT Name FROM CommunicationTool.dbo.Campaign WHERE CompanyId = '" + companyId + "'";
        String campaignNameInDb = connector.getStringValueInDB(queryCampaign, "Name");
        connector.closeConnection();
        return campaignNameInDb.equals(campaignName);
    }

    @Step("Delete company in the database")
    public void deleteCampaignInDB(String campaignName) {
        LOG.info("Get campaign id in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT Id FROM CommunicationTool.dbo.Campaign WHERE Name = '" + campaignName + "'";
        String id = connector.getStringValueInDB(query, "Id");
        LOG.info("Delete email communication in the database where CampaignId = '" + id + "'");
        connector.executeQuery("DELETE FROM CommunicationTool.dbo.EmailCommunication WHERE CampaignId = '" + id + "'");
        LOG.info("Successfully deleted");
        LOG.info("Delete representative campaign in the database where CampaignId = '" + id + "'");
        connector.executeQuery("DELETE FROM CommunicationTool.dbo.RepresentativeCampaign WHERE CampaignId = '" + id + "'");
        LOG.info("Successfully deleted");
        LOG.info("Delete campaign in the database where campaign name = '" + campaignName + "'");
        connector.executeQuery("DELETE FROM CommunicationTool.dbo.Campaign WHERE Name = '" + campaignName + "'");
        LOG.info("Successfully deleted");
        connector.closeConnection();
    }
}
