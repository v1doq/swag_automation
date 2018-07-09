package projects.com.communication.tool.campaign;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.ContactsStep;
import projects.com.communication.tool.steps.campaign.CampaignStep;
import projects.com.communication.tool.steps.campaign.FlowStep;
import projects.com.communication.tool.steps.campaign.RepresentativeStep;
import projects.com.communication.tool.steps.contacts.FilterStep;

import static common.ConciseApi.sleep;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.*;
import static projects.com.communication.tool.steps.campaign.ContactsStep.WORK_EMAIL_TYPE;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;
import static projects.com.communication.tool.steps.campaign.FlowStep.WORK_EMAIL_CHANNEL;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.GATEWAY_OUTLOOK_EMAIL;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.MIN_FROM_NAME_LENGTH;

@Feature("Campaign's filters")
@Story("Functional tests for filters tab in campaign")
public class CampaignContactsTest extends SuiteTestCT {

    private FilterStep filterStep;
    private CampaignStep campaignStep;
    private ContactsStep contactsStep;
    private RepresentativeStep repStep;
    private FlowStep flowStep;
    private String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        campaignStep = new CampaignStep(driver);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        filterStep = new FilterStep(driver);
        contactsStep = new ContactsStep(driver);
        repStep = new RepresentativeStep(driver);
        flowStep = new FlowStep(driver);
        contactsStep.deleteAllFiltersInCampaignDb();
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "sanity campaign"}, description = "Add filter to campaign")
    public void addFilterToCampaign() {
        String firstName = randomAlphabetic(5);
        String email = randomAlphabetic(5) + "@i.ua";
        contactsStep.insertContactToDb(firstName, email, WORK_EMAIL_TYPE);
        goToContactsTab();

        contactsStep.openAddFiltersPopUp();
        int count = filterStep.setFiltersByFirstName(firstName);
        contactsStep.addFilterToCampaign();

        assertTrue(contactsStep.isFilterDisplayedInTable(firstName, count));
        contactsStep.deleteContactFromDb(firstName, email);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Add filter to campaign without email")
    public void addFilterToCampaignWithoutEmail() {
        String firstName = randomAlphabetic(5);
        contactsStep.insertContactToDb(firstName);
        goToContactsTab();

        contactsStep.openAddFiltersPopUp();
        int count = filterStep.setFiltersByFirstName(firstName);
        contactsStep.addFilterToCampaign();

        assertTrue(contactsStep.isFilterDisplayedInTable(firstName, count));
        contactsStep.deleteContactFromDb(firstName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Edit campaign's filter")
    public void editCampaignsFilter() {
        String firstName = randomAlphabetic(5);
        contactsStep.insertContactToDb(firstName);
        contactsStep.insertFilterToCampaignDb(campaignName, randomAlphabetic(5));
        goToContactsTab();

        contactsStep.openEditFiltersPopUp();
        int count = filterStep.changeValueInFilter(firstName);
        contactsStep.addFilterToCampaign();
        sleep(2000);

        assertTrue(contactsStep.isFilterDisplayedInTable(firstName, count));
        contactsStep.deleteContactFromDb(firstName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Delete campaign's filter")
    public void deleteCampaignsFilter() {
        String firstName = randomAlphabetic(5);
        contactsStep.insertContactToDb(firstName);
        contactsStep.insertFilterToCampaignDb(campaignName, randomAlphabetic(5));
        goToContactsTab();

        contactsStep.deleteFilter();

        assertFalse(contactsStep.isFilterDisplayedInTable(firstName, 1));
        contactsStep.deleteContactFromDb(firstName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Try to activate campaign without contacts")
    public void tryToActivateCampaignWithoutContacts() {
        campaignStep.selectCampaignInList(campaignName);
        repStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, randomAlphabetic(MIN_FROM_NAME_LENGTH));
        flowStep.saveFlowWithoutTemplate(WORK_EMAIL_CHANNEL);

        campaignStep.activateCommunication();

        assertTrue(campaignStep.isServerErrorDisplayed(NO_CONTACT_FOUND));
    }

    private void goToContactsTab() {
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        contactsStep.openContactsTab();
    }
}
