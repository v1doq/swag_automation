package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import projects.com.communication.tool.components.CampaignComponent;
import settings.SQLConnector;

import static common.ConciseApi.sleep;
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

    @Step("Verify that company is displayed in list")
    public boolean isCompanyDisplayedInList(String companyName) {
        sleep(1000);
        return component.isTextDisplayed(companyName, component.getCompanyInList());
    }

    @Step("Verify that campaign is displayed in list")
    public boolean isCampaignDisplayedInList(String campaignName) {
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
}
