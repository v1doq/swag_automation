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

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.FiltersStep.EQUAL_CRITERION;
import static projects.com.communication.tool.steps.FiltersStep.FIRST_NAME_FILTER;
import static projects.com.communication.tool.steps.RepresentativeStep.GATEWAY_OUTLOOK_EMAIL;
import static projects.com.communication.tool.steps.RepresentativeStep.MIN_FROM_NAME_LENGTH;
import static settings.SQLConnector.EQUAL;

@Feature("Campaign")
@Story("Functional tests for campaign")
public class CampaignTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private RepresentativeStep repsStep;
    private ContactsStep contactsStep;
    private FiltersStep filtersStep;
    private ScheduleStep scheduleStep;
    private TemplateStep templateStep;
    private String campaignName = randomAlphabetic(5);
    private String companyName = randomAlphabetic(5);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        cleanDatabase();
        campaignStep = new CampaignStep(driver);
        campaignStep.createCampaignInDB(campaignName, companyName);
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        contactsStep = new ContactsStep(driver);
        scheduleStep = new ScheduleStep(driver);
        filtersStep = new FiltersStep(driver);
        repsStep = new RepresentativeStep(driver);
        templateStep = new TemplateStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create new company and add campaign to it")
    public void createNewCompanyAndAddCampaignToIt() {
        String campaignName = randomAlphabetic(5);
        String companyName = randomAlphabetic(5);
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCompanyDisplayedInList(companyName));
        assertTrue(campaignStep.isCampaignDisplayedInList(campaignName));
        assertTrue(campaignStep.isCampaignAssignToCompany(companyName, campaignName));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Add campaign to existing company")
    public void addCampaignToExistingCompany() {
        String campaignName = randomAlphabetic(5);
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCompanyDisplayedInList(companyName));
        assertTrue(campaignStep.isCampaignDisplayedInList(campaignName));
        assertTrue(campaignStep.isCampaignAssignToCompany(companyName, campaignName));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create new representative")
    public void createNewRepresentative() {
        String fromName = randomAlphabetic(MIN_FROM_NAME_LENGTH);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        repsStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, fromName);
        assertTrue(repsStep.isFromNameDisplayedInRepsCard(fromName));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Add contacts to campaign")
    public void addContactsToCampaign() {
        String value = "Varg";
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        contactsStep.openContactsTab();

        contactsStep.openContactsPopUp();
        filtersStep.applyAllFilters(FIRST_NAME_FILTER, EQUAL_CRITERION, value);
        int count = filtersStep.getValueByCriterion("FirstName", EQUAL, value);

        filtersStep.waitForRecordsResult(count);
        assertEquals(filtersStep.getRecordsCounter(), String.valueOf(count));

        contactsStep.saveContactsInCampaign();
        assertTrue(contactsStep.isContactsAddedToCampaign(value));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Update schedule parameters")
    public void updateScheduleParametersAndCheckItInDb() {
        String interval = "1";
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        scheduleStep.openScheduleTab();
        scheduleStep.updateSchedule(interval);
        String intervalInDb = scheduleStep.getScheduleIntervalInDB(campaignName);

        assertEquals(intervalInDb, interval);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Update template values")
    public void updateTemplateAndCheckItInDb() {
        String subj = randomAlphabetic(10);
        String body = randomAlphabetic(10);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        templateStep.openTemplateTab();
        templateStep.updateTemplate(subj, body);
        String subjInDb = templateStep.getTemplateSubjInDB(campaignName);

        assertEquals(subjInDb, subj);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "regression", description = "Check validation messages for empty mandatory fields")
    public void checkValidationMessagesForAddNewCampaignFields() {
        campaignStep.openCampaignPage();
        campaignStep.openCampaignPopUp();
        campaignStep.submitCampaign();

        assertTrue(campaignStep.isCampaignErrorDisplayed());
        assertTrue(campaignStep.isCompanyErrorDisplayed());
    }
}
