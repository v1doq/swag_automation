package projects.com.communication.tool.campaign;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.CampaignStep;
import projects.com.communication.tool.steps.campaign.ContactsStep;
import projects.com.communication.tool.steps.contacts.FiltersStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.*;
import static projects.com.communication.tool.steps.campaign.CampaignStep.MIN_CAMPAIGN_NAME_LENGTH;
import static projects.com.communication.tool.steps.campaign.CampaignStep.MIN_COMPANY_NAME_LENGTH;
import static projects.com.communication.tool.steps.contacts.FiltersStep.EQUAL_CRITERION;
import static projects.com.communication.tool.steps.contacts.FiltersStep.FIRST_NAME_FILTER;
import static settings.SQLConnector.EQUAL;

@Feature("Campaign")
@Story("Functional tests for contacts tab in campaign")
public class ContactsTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private ContactsStep contactsStep;
    private FiltersStep filtersStep;
    private String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        campaignStep = new CampaignStep(driver);
        filtersStep = new FiltersStep(driver);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        contactsStep = new ContactsStep(driver);
        filtersStep = new FiltersStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Add contacts to campaign")
    public void addContactsToCampaign() {
        String firstName = randomAlphabetic(5);
        String email = randomAlphabetic(5) + "@i.ua";
        filtersStep.insertContactToDb(firstName, email);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        contactsStep.openContactsTab();

        contactsStep.openContactsPopUp();
        filtersStep.applyAllFilters(FIRST_NAME_FILTER, EQUAL_CRITERION, firstName);
        int count = filtersStep.getValueByCriterion("FirstName", EQUAL, firstName);
        filtersStep.waitForRecordsResult(count);
        assertEquals(filtersStep.getRecordsCounter(), String.valueOf(count));
        contactsStep.saveContactsInCampaign();

        assertTrue(contactsStep.isContactsAddedToCampaign(firstName));
        filtersStep.deleteContactFromDb(firstName, email);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Try to add the same contact twice")
    public void tryToAddContactTwice() {
        String firstName = randomAlphabetic(5);
        String email = randomAlphabetic(5) + "@i.ua";
        filtersStep.insertContactToDb(firstName, email);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        contactsStep.openContactsTab();

        int count = contactsStep.addContactToCampaign(firstName);
        contactsStep.addContactToCampaign(firstName);

        assertEquals(count, contactsStep.getContactsListSizeByText(firstName));
        filtersStep.deleteContactFromDb(firstName, email);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Try to add contact without email")
    public void tryToAddContactWithoutEmail() {
        String firstName = randomAlphabetic(5);
        filtersStep.insertContactToDb(firstName);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        contactsStep.openContactsTab();

        contactsStep.addContactToCampaign(firstName);
        assertFalse(contactsStep.isContactsAddedToCampaign(firstName));

        filtersStep.deleteContactFromDb(firstName);
    }
}
