package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.FlowComponent;
import projects.com.communication.tool.components.campaign.TemplateComponent;
import settings.SQLConnector;

import java.util.List;

import static common.ConciseApi.sleep;
import static settings.SeleniumListener.LOG;

public class TemplateStep {

    private TemplateComponent component;
    private FlowComponent flowComponent;

    public TemplateStep(WebDriver driver) {
        this.component = new TemplateComponent(driver);
        this.flowComponent = new FlowComponent(driver);
    }

    @Step("Add placeholders to template")
    public void addPlaceholderToTemplate(List<String> list) {
        for (String placeholder: list) {
            component.clickToElementInListByText(placeholder, component.getPlaceholderButton());
        }
    }

    @Step("Create template")
    public void createTemplate(String subj, String body) {
        flowComponent.getFlowCard().click();
        sleep(2000);
        component.getSubjectInput().sendKeys(subj);
        component.getDriver().switchTo().frame(0);
        component.getBodyInput().sendKeys(body);
        component.getDriver().switchTo().defaultContent();
    }

    @Step("Verify that flow is successfully created")
    public String getTemplateSubjInDB(String campaignName) {
        LOG.info("Get campaign id in the database");
        SQLConnector connector = new SQLConnector();
        String campaignId = connector.getStringValueInDB("SELECT Id FROM CommunicationTool.dbo." +
                "Campaign WHERE Name = '" + campaignName + "'", "Id");
        LOG.info("Get subject in the database");
        return connector.getStringValueInDB("SELECT Template_Subject FROM CommunicationTool.dbo." +
                "Flow WHERE CampaignId = '" + campaignId + "'", "Template_Subject");
    }
}
