package projects.com.communication.tool.campaign;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.*;

import javax.mail.Message;
import javax.mail.MessagingException;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.*;
import static projects.com.communication.tool.steps.campaign.CampaignFiltersStep.*;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;
import static projects.com.communication.tool.steps.campaign.FlowStep.WORK_EMAIL_CHANNEL;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.*;
import static projects.com.communication.tool.steps.campaign.TemplateStep.*;
import static projects.com.communication.tool.steps.communication.MailStep.*;
import static settings.SeleniumListener.LOG;

@Feature("Campaign's flow")
@Story("Functional tests for flow tab in campaign")
public class CampaignFlowTest extends SuiteTestCT {

    private FlowStep flowStep;
    private CampaignStep campaignStep;
    private TemplateStep templateStep;
    private RepresentativeStep repsStep;
    private CampaignFiltersStep campaignFiltersStep;
    private String subj = randomAlphabetic(MIN_SUBJECT_LENGTH);
    private String body = randomAlphabetic(MIN_BODY_LENGTH);

    @BeforeMethod(description = "Preconditions", alwaysRun = true)
    public void setUp() {
        flowStep = new FlowStep(driver);
        campaignStep = new CampaignStep(driver);
        templateStep = new TemplateStep(driver);
        repsStep = new RepresentativeStep(driver);
        campaignFiltersStep = new CampaignFiltersStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "sanity campaign"}, description = "Create flow with template")
    public void createFlowWithTemplate() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        prepareCampaign(campaignName);
        prepareFlow();
        assertTrue(flowStep.isTypeAppliedToFlow(WORK_EMAIL_CHANNEL));

        templateStep.prepareTemplate(subj, body);
        flowStep.saveFlow();

        String subjInDb = templateStep.getTemplateSubjInDB(campaignName);
        assertEquals(subjInDb, subj);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"sanity campaign"}, description = "Create flow without template")
    public void createFlowWithoutTemplate() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        prepareCampaign(campaignName);
        prepareFlow();
        flowStep.saveFlow();

        assertNull(templateStep.getTemplateSubjInDB(campaignName));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"sanity campaign"}, timeOut = 180000, description = "Send test template to email")
    public void sendTestTemplateToEmail() throws MessagingException {
        String firstName = randomAlphabetic(5);
        String contactInfo = randomAlphabetic(5) + "@i.ua";
        String fromName = randomAlphabetic(MIN_FROM_NAME_LENGTH);
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        String email = GATEWAY_GMAIL_EMAIL;
        cleanMailFolders();
        campaignFiltersStep.insertContactToDb(firstName, contactInfo, WORK_EMAIL_TYPE);
        prepareCampaign(campaignName);
        repsStep.createRepresentative(email, fromName);
        prepareFlow();

        templateStep.prepareTemplate(subj, body);
        templateStep.sendTestTemplateToEmail(contactInfo, TEST_EMAIL);
        assertTrue(templateStep.isSuccessMessageDisplayed());

        openMailFolder(INBOX);
        Message[] messages = receiveMail(email, 1);
        for (Message message : messages) {
            String messageBody = getMessageBody(message);
            assert messageBody != null;
            LOG.info("Subject: " + message.getSubject());
            LOG.info("From: " + message.getFrom()[0]);
            LOG.info("Text: " + messageBody);
            assertEquals(message.getSubject(), subj);
            assertTrue(message.getFrom()[0].toString().contains(email));
            assertTrue(messageBody.contains(body));
        }
        campaignFiltersStep.deleteContactFromDb(firstName, contactInfo);
    }

    private void prepareCampaign(String campaignName) {
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
    }

    private void prepareFlow(){
        flowStep.openFlowTab();
        flowStep.selectFlow(WORK_EMAIL_CHANNEL);
        flowStep.openFlowDialog();
    }
}
