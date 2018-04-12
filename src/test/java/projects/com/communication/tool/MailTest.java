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
import static projects.com.communication.tool.steps.GatewayStep.*;
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
    private String fromName = "Communication " + randomAlphanumeric(5);
    private String subj = "Hi, this is " + randomAlphabetic(5);
    private String body = "Good morning. Have a good day, see you soon " + randomAlphabetic(5);

    @BeforeClass(description = "Clean the database", alwaysRun = true)
    public void cleanDbAndCreateCompany() {
        cleanDatabase();
        cleanMailFolders();
    }

    @BeforeMethod(description = "Precondition for email sending", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        contactsStep = new ContactsStep(driver);
        scheduleStep = new ScheduleStep(driver);
        gatewayStep = new GatewayStep(driver);
        templateStep = new TemplateStep(driver);
        loginWithToken();
        String campaignName = randomAlphabetic(5);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(5));
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "mail", timeOut = 180000, description = "Create gmail communication and check mail")
    public void createGmailCommunicationAndCheckMail() throws MessagingException, IOException {
        String fromEmail = GATEWAY_GMAIL_EMAIL;
        int messageCount = sendEmailsBy(GMAIL);

        openMailFolder(INBOX);
        Message[] messages = receiveMail(fromEmail, messageCount);
        assertEquals(messages.length, messageCount);

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

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "mail", timeOut = 240000, description = "Create outlook communication and check mail")
    public void createOutlookCommunicationAndCheckMail() throws MessagingException, IOException {
        String fromEmail = GATEWAY_OUTLOOK_EMAIL;
        int messageCount = sendEmailsBy(OUTLOOK);

        openMailFolder(INBOX);
        Message[] messages = receiveMail(fromEmail, messageCount);
        assertEquals(messages.length, messageCount);

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

    private int sendEmailsBy(String mailer){
        gatewayStep.createGateway(mailer, fromName);
        templateStep.openTemplateTab();
        templateStep.updateTemplate(subj, body);
        scheduleStep.openScheduleTab();
        scheduleStep.updateSchedule("5");
        contactsStep.openContactsTab();
        int contactsCount = contactsStep.addContactToCampaign("450 Massachusetts Ave NW");
        campaignStep.activateCommunication();
        return contactsCount;
    }
}
