package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.TemplateComponent;
import settings.SQLConnector;

import java.util.List;

import static settings.SeleniumListener.LOG;

public class TemplateStep {

    private TemplateComponent component;
    public static final int MIN_SUBJECT_LENGTH = 1;
    public static final int MIN_BODY_LENGTH = 1;

    public TemplateStep(WebDriver driver) {
        this.component = new TemplateComponent(driver);
    }

    @Step("Prepare template")
    public void prepareTemplate(String subj, String body) {
        component.getSubjectInput().sendKeys(subj);
        component.getDriver().switchTo().frame(0);
        component.getBodyInput().sendKeys(body);
        component.getDriver().switchTo().defaultContent();
    }

    @Step("Add placeholders to template")
    public void addPlaceholderToTemplate(List<String> list) {
        for (String placeholder: list) {
            component.getElementInListByText(placeholder, component.getPlaceholderButton()).click();
        }
    }

    @Step("Send test template to email")
    public void sendTestTemplateToEmail(String contactInfo, String email) {
        component.getContactInfoInput().sendKeys(contactInfo);
        component.getTestingEmailInput().sendKeys(email);
        component.getSendButton().click();
    }

    @Step("Send test template to email")
    public boolean isSuccessMessageDisplayed() {
        String text = "Test email sent successfully";
        component.waitForText(component.getTestStatus(), text);
        return component.isTextDisplayed(text, component.getTestStatus());
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
