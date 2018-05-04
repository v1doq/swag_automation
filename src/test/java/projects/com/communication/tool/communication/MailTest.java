package projects.com.communication.tool.communication;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.*;

import javax.mail.Message;
import javax.mail.MessagingException;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.communication.MailStep.*;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.*;
import static settings.SeleniumListener.LOG;

@Feature("Communication")
@Story("Sending and receiving emails")
public class MailTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private RepresentativeStep repsStep;
    private ContactsStep contactsStep;
    private ScheduleStep scheduleStep;
    private TemplateStep templateStep;

    @BeforeClass(description = "Clean the mail folder", alwaysRun = true)
    public void cleanMailFolder() {
        cleanMailFolders();
    }

    @BeforeMethod(description = "Precondition for email sending", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        contactsStep = new ContactsStep(driver);
        scheduleStep = new ScheduleStep(driver);
        repsStep = new RepresentativeStep(driver);
        templateStep = new TemplateStep(driver);
        loginWithToken();
        String campaignName = randomAlphabetic(5);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(5));
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "mail", timeOut = 180000, dataProvider = "Mail", description = "Create communication and check mail")
    public void createCommunicationAndCheckMail(String fromEmail) throws MessagingException {
        String fromName = "Communication " + randomAlphanumeric(5);
        String subj = "Hi, this is " + randomAlphabetic(5);
        String body = "Good morning. Have a good day, see you soon " + randomAlphabetic(5);
        String key = randomAlphabetic(1).toUpperCase() + randomAlphabetic(5).toLowerCase();
        String value = randomAlphabetic(5);

        repsStep.createRepsWithPlaceholder(fromEmail, fromName, key, value);
        templateStep.openTemplateTab();
        templateStep.createTemplateWithPlaceholders(subj, body, "Name", "Email", key);
        scheduleStep.openScheduleTab();
        scheduleStep.updateSchedule("5");
        contactsStep.openContactsTab();
        int messageCount = contactsStep.addContactToCampaign("Regulatory Affairs Associate");
        campaignStep.activateCommunication();

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
            assertTrue(messageBody.contains(body + fromName + fromEmail + value));
        }
    }

    @DataProvider(name = "Mail")
    public Object[][] credentials() {
        return new Object[][]{
                {GATEWAY_GMAIL_EMAIL},
                {GATEWAY_OUTLOOK_EMAIL}
        };
    }
}
