package projects.com.communication.tool;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.*;

import javax.mail.Message;
import javax.mail.MessagingException;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.MailStep.*;
import static projects.com.communication.tool.steps.RepresentativeStep.*;
import static settings.SeleniumListener.LOG;

@Feature("Communication")
@Story("Sending and receiving emails")
public class MailTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private RepresentativeStep repsStep;
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
    public void createCommunicationAndCheckMail(String email) throws MessagingException {
        repsStep.createRepresentative(email, fromName);
        templateStep.openTemplateTab();
        templateStep.updateTemplate(subj, body);
        scheduleStep.openScheduleTab();
        scheduleStep.updateSchedule("5");
        contactsStep.openContactsTab();
        int messageCount = contactsStep.addContactToCampaign("Human Resources Coordinator");
        campaignStep.activateCommunication();

        openMailFolder(INBOX);
        Message[] messages = receiveMail(email, messageCount);
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
            assertTrue(message.getFrom()[0].toString().contains(email));
            assertTrue(messageBody.contains(body));
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
