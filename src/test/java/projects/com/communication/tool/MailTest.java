package projects.com.communication.tool;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.*;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.io.IOException;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.GatewayStep.GATEWAY_GMAIL_EMAIL;
import static settings.MailReader.*;
import static settings.SeleniumListener.LOG;

@Feature("Communication")
@Story("Sending and receiving emails")
public class MailTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private GatewayStep gatewayStep;
    private ContactsStep contactsStep;
    private ScheduleStep scheduleStep;
    private TemplateStep templateStep;

    @BeforeClass(description = "Clean database and mail", alwaysRun = true)
    public void setUp() {
        cleanDatabase();
        openMailFolder();
        deleteMessages();
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void init() {
        campaignStep = new CampaignStep(driver);
        contactsStep = new ContactsStep(driver);
        scheduleStep = new ScheduleStep(driver);
        gatewayStep = new GatewayStep(driver);
        templateStep = new TemplateStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "mail", timeOut = 180000, description = "Create communication and check mail")
    public void createCommunicationAndCheckMail() throws MessagingException, IOException {
        String fromName = "Communication " + randomAlphanumeric(5);
        String subj = "Hi, this is " + randomAlphabetic(5);
        String body = "Good morning. Have a good day, see you soon " + randomAlphabetic(5);
        String fromEmail = GATEWAY_GMAIL_EMAIL;
        String campaignName = randomAlphabetic(5);
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(campaignName, randomAlphabetic(5));
        campaignStep.selectCampaignInList(campaignName);

        gatewayStep.createGmailGateway(fromName);
        scheduleStep.openScheduleTab();
        scheduleStep.updateSchedule("5");
        templateStep.openTemplateTab();
        templateStep.updateTemplate(subj, body);
        contactsStep.openContactsTab();
        int contactsCount = contactsStep.addContactToCampaign("523 Broadway E");
        campaignStep.activateCommunication();

        openMailFolder();
        Message[] messages = receiveMail(fromEmail, contactsCount);
        assertEquals(messages.length, contactsCount);

        for (int i = 0; i < messages.length; i++) {
            Message message = messages[i];
            String messageBody = getMessageBody(message);
            assert messageBody != null;
            LOG.info("Email Number " + (i + 1));
            LOG.info("Subject: " + message.getSubject());
            LOG.info("From: " + message.getFrom()[0]);
            LOG.info("Text: " + messageBody);
            assertEquals(message.getSubject(), subj);
            assertTrue(message.getFrom()[0].toString().contains(fromEmail));
            assertTrue(messageBody.contains(body));
        }
    }
}
