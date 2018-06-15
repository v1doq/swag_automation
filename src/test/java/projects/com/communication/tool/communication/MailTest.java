package projects.com.communication.tool.communication;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.*;
import projects.com.communication.tool.steps.campaign.*;
import projects.com.communication.tool.steps.contacts.FilterStep;

import javax.mail.Message;
import javax.mail.MessagingException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.CampaignFiltersStep.WORK_EMAIL_TYPE;
import static projects.com.communication.tool.steps.campaign.FlowStep.WORK_EMAIL;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.GATEWAY_GMAIL_EMAIL;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.GATEWAY_OUTLOOK_EMAIL;
import static projects.com.communication.tool.steps.communication.MailStep.*;
import static settings.SeleniumListener.LOG;

@Feature("Communication")
@Story("Sending and receiving emails")
public class MailTest extends SuiteTestCT {

    private FlowStep flowStep;
    private FilterStep filterStep;
    private CampaignStep campaignStep;
    private TemplateStep templateStep;
    private RepresentativeStep repsStep;
    private CampaignFiltersStep filtersTabStep;
    private String firstName = randomAlphabetic(5);
    private String email = randomAlphabetic(5) + "@i.ua";

    @BeforeClass(description = "Clean the mail folder and insert contact to DB", alwaysRun = true)
    public void cleanMailFolder() {
        cleanMailFolders();
        filtersTabStep = new CampaignFiltersStep(driver);
        filtersTabStep.insertContactToDb(firstName, email, WORK_EMAIL_TYPE);
    }

    @AfterClass(description = "Delete contact from the database", alwaysRun = true)
    public void deleteContactFromDb() {
        filtersTabStep = new CampaignFiltersStep(driver);
        filtersTabStep.deleteContactFromDb(firstName, email);
    }

    @BeforeMethod(description = "Precondition for email sending", alwaysRun = true)
    public void setUp() {
        flowStep = new FlowStep(driver);
        filterStep = new FilterStep(driver);
        campaignStep = new CampaignStep(driver);
        templateStep = new TemplateStep(driver);
        repsStep = new RepresentativeStep(driver);
        filtersTabStep = new CampaignFiltersStep(driver);
        loginWithToken();
        createAndSelectCampaign();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "mail", timeOut = 300000, dataProvider = "Mail", description = "Create communication and check mail")
    public void createCommunicationAndCheckMail(String fromEmail) throws MessagingException {
        String fromName = "Communication " + randomAlphanumeric(5);
        String subj = "Hi, this is " + randomAlphabetic(5);
        String body = "Have a good day, see you soon " + randomAlphabetic(5);
        String repsKey = randomAlphabetic(1).toUpperCase() + randomAlphabetic(5).toLowerCase();
        String repsValue = randomAlphabetic(5);
        List<String> placeholders = asList("First Name", "Name", "Email", repsKey);

        repsStep.createRepsWithPlaceholder(fromEmail, fromName, repsKey, repsValue);
        int count = addFilters();
        createFlow(subj, body, placeholders);
        campaignStep.activateCommunication();

        openMailFolder(INBOX);
        Message[] messages = receiveMail(fromEmail, count);
        assertEquals(messages.length, count);
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

    private int addFilters(){
        filtersTabStep.openFiltersTab();
        filtersTabStep.openAddFiltersPopUp();
        int messageCount = filterStep.setFiltersByFirstName(firstName);
        filtersTabStep.addFilterToCampaign();
        return messageCount;
    }

    private void createFlow(String subj, String body, List<String> placeholders){
        flowStep.openFlowTab();
        flowStep.createFlow(WORK_EMAIL);
        templateStep.createTemplate(subj, body);
        templateStep.addPlaceholderToTemplate(placeholders);
        flowStep.saveFlow();
    }

    private void createAndSelectCampaign(){
        String campaignName = randomAlphabetic(5);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(5));
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
    }
}
