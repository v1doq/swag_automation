package projects.com.communication.tool.campaign;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.CampaignFiltersStep;
import projects.com.communication.tool.steps.campaign.CampaignStep;
import projects.com.communication.tool.steps.contacts.FilterStep;

import static common.ConciseApi.sleep;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.*;
import static projects.com.communication.tool.steps.campaign.CampaignFiltersStep.WORK_EMAIL_TYPE;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;

@Feature("Campaign's filters")
@Story("Functional tests for filters tab in campaign")
public class CampaignFiltersTest extends SuiteTestCT {

    private FilterStep filterStep;
    private CampaignStep campaignStep;
    private CampaignFiltersStep filtersTabStep;
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
        filtersTabStep = new CampaignFiltersStep(driver);
        filtersTabStep.deleteAllFiltersInCampaignDb();
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "sanity campaign"}, description = "Add filter to campaign")
    public void addFilterToCampaign() {
        String firstName = randomAlphabetic(5);
        String email = randomAlphabetic(5) + "@i.ua";
        filtersTabStep.insertContactToDb(firstName, email, WORK_EMAIL_TYPE);
        goToFilterTab();

        filtersTabStep.openAddFiltersPopUp();
        int count = filterStep.setFiltersByFirstName(firstName);
        filtersTabStep.addFilterToCampaign();

        assertTrue(filtersTabStep.isFilterDisplayedInTable(firstName, count));
        filtersTabStep.deleteContactFromDb(firstName, email);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Add filter to campaign without email")
    public void addFilterToCampaignWithoutEmail() {
        String firstName = randomAlphabetic(5);
        filtersTabStep.insertContactToDb(firstName);
        goToFilterTab();

        filtersTabStep.openAddFiltersPopUp();
        int count = filterStep.setFiltersByFirstName(firstName);
        filtersTabStep.addFilterToCampaign();

        assertTrue(filtersTabStep.isFilterDisplayedInTable(firstName, count));
        filtersTabStep.deleteContactFromDb(firstName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Edit campaign's filter")
    public void editCampaignsFilter() {
        String firstName = randomAlphabetic(5);
        filtersTabStep.insertContactToDb(firstName);
        filtersTabStep.insertFilterToCampaignDb(campaignName, randomAlphabetic(5));
        goToFilterTab();

        filtersTabStep.openEditFiltersPopUp();
        int count = filterStep.changeValueInFilter(firstName);
        filtersTabStep.addFilterToCampaign();
        sleep(2000);

        assertTrue(filtersTabStep.isFilterDisplayedInTable(firstName, count));
        filtersTabStep.deleteContactFromDb(firstName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Delete campaign's filter")
    public void deleteCampaignsFilter() {
        String firstName = randomAlphabetic(5);
        filtersTabStep.insertContactToDb(firstName);
        filtersTabStep.insertFilterToCampaignDb(campaignName, randomAlphabetic(5));
        goToFilterTab();

        filtersTabStep.deleteFilter();

        assertFalse(filtersTabStep.isFilterDisplayedInTable(firstName, 1));
        filtersTabStep.deleteContactFromDb(firstName);
    }

    private void goToFilterTab() {
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        filtersTabStep.openFiltersTab();
    }
}
