package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.TemplateComponent;
import settings.SQLConnector;

import static settings.SeleniumListener.LOG;

public class TemplateStep {

    private TemplateComponent component;

    public TemplateStep(WebDriver driver) {
        this.component = new TemplateComponent(driver);
    }

    @Step("Open template tab")
    public void openTemplateTab() {
        component.scrollUp();
        component.getTemplateTab().click();
    }

    @Step("Add placeholder to template")
    public void addPlaceholderToTemplate(String repsName, String repsEmail, String repsKey, String contactData) {
        component.clickToElementInListByText(repsName, component.getRepsPlaceholderButton());
        component.clickToElementInListByText(repsEmail, component.getRepsPlaceholderButton());
        component.clickToElementInListByText(repsKey, component.getRepsPlaceholderButton());
        component.clickToElementInListByText(contactData, component.getContactPlaceholderButton());
    }

    @Step("Add subject, body and update template")
    public void updateTemplate(String subj, String body) {
        component.getSubjectInput().sendKeys(subj);
        component.getBodyInput().sendKeys(body);
        component.getSaveButton().click();
    }

    @Step("Verify that template is successfully updated")
    public String getTemplateSubjInDB(String campaignName) {
        LOG.info("Get campaign id in the database");
        SQLConnector connector = new SQLConnector();
        String campaignId = connector.getStringValueInDB("SELECT Id FROM CommunicationTool.dbo." +
                "Campaign WHERE Name = '" + campaignName + "'", "Id");
        LOG.info("Get subject in the database");
        String subj = connector.getStringValueInDB("SELECT Template_Subject FROM CommunicationTool.dbo." +
                "EmailCommunication WHERE CampaignId = '" + campaignId + "'", "Template_Subject");
        connector.closeConnection();
        return subj;
    }
}
