package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.TemplateComponent;
import settings.SQLConnector;

import java.util.List;

import static projects.com.communication.tool.common.CommStackDB.*;
import static settings.SQLConnector.*;

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
        for (String placeholder : list) {
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
        SQLConnector con = new SQLConnector();
        String campaignId = con.getValueInDb(SELECT_FROM + CAMPAIGN_DB + WHERE + "Name = '" + campaignName + "'", "Id");
        return con.getValueInDb(SELECT_FROM + FLOW_DB + WHERE + "CampaignId = '" + campaignId + "'", "Template_Subject");
    }
}
