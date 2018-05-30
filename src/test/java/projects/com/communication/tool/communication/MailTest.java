package projects.com.communication.tool.communication;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.*;
import projects.com.communication.tool.steps.campaign.*;

import javax.mail.Message;
import javax.mail.MessagingException;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.ContactsStep.WORK_EMAIL_TYPE;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.*;
import static projects.com.communication.tool.steps.communication.MailStep.*;
import static settings.SeleniumListener.LOG;

@Feature("Communication")
@Story("Sending and receiving emails")
public class MailTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private RepresentativeStep repsStep;
    private ContactsStep contactsStep;
    private ScheduleStep scheduleStep;
    private TemplateStep templateStep;
    private String firstName = randomAlphabetic(5);
    private String email = randomAlphabetic(5) + "@i.ua";

    @BeforeClass(description = "Clean the mail folder", alwaysRun = true)
    public void cleanMailFolder() {
        cleanMailFolders();
        contactsStep = new ContactsStep(driver);
        contactsStep.insertContactToDb(firstName, email, WORK_EMAIL_TYPE);
    }

    @AfterClass(description = "Delete contact from the database", alwaysRun = true)
    public void deleteContactFromDb() {
        contactsStep = new ContactsStep(driver);
        contactsStep.deleteContactFromDb(firstName, email);
    }

    @BeforeMethod(description = "Precondition for email sending", alwaysRun = true)
    public void setUp() {
        String campaignName = randomAlphabetic(5);
        campaignStep = new CampaignStep(driver);
        contactsStep = new ContactsStep(driver);
        scheduleStep = new ScheduleStep(driver);
        repsStep = new RepresentativeStep(driver);
        templateStep = new TemplateStep(driver);
        loginWithToken();
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
        String repsKey = randomAlphabetic(1).toUpperCase() + randomAlphabetic(5).toLowerCase();
        String repsValue = randomAlphabetic(5);

        repsStep.createRepsWithPlaceholder(fromEmail, fromName, repsKey, repsValue);
        templateStep.openTemplateTab();
        templateStep.addPlaceholderToTemplate("Name", "Email", repsKey, "First Name");
        templateStep.updateTemplate(subj, body);
        scheduleStep.openScheduleTab();
        scheduleStep.updateSchedule("5");
        contactsStep.openContactsTab();
        int messageCount = contactsStep.addContactToCampaign(firstName);
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
            assertTrue(messageBody.contains(body));
            assertTrue(messageBody.contains(fromName));
            assertTrue(messageBody.contains(fromEmail));
            assertTrue(messageBody.contains(repsValue));
            assertTrue(messageBody.contains(firstName));
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
