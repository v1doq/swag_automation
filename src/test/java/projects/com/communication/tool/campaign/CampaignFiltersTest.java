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
import projects.com.communication.tool.steps.campaign.CampaignFiltersStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.CampaignStep.MIN_CAMPAIGN_NAME_LENGTH;
import static projects.com.communication.tool.steps.campaign.CampaignStep.MIN_COMPANY_NAME_LENGTH;
import static projects.com.communication.tool.steps.campaign.CampaignFiltersStep.WORK_EMAIL_TYPE;

@Feature("Campaign's filters")
@Story("Functional tests for filters tab in campaign")
public class CampaignFiltersTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private CampaignFiltersStep campaignFiltersStep;
    private String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        campaignStep = new CampaignStep(driver);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        campaignFiltersStep = new CampaignFiltersStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "sanity campaign"}, description = "Add filter to campaign")
    public void addFilterToCampaign() {
        String firstName = randomAlphabetic(5);
        String email = randomAlphabetic(5) + "@i.ua";
        campaignFiltersStep.insertContactToDb(firstName, email, WORK_EMAIL_TYPE);
        goToFilterTab();

        int count = campaignFiltersStep.addFilterToCampaign(firstName);
        assertTrue(campaignFiltersStep.isFilterDisplayedInTable(firstName, count));
        campaignFiltersStep.deleteContactFromDb(firstName, email);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Add filter without email")
    public void AddFilterWithoutEmail() {
        String firstName = randomAlphabetic(5);
        campaignFiltersStep.insertContactToDb(firstName);
        goToFilterTab();

        int count = campaignFiltersStep.addFilterToCampaign(firstName);
        assertTrue(campaignFiltersStep.isFilterDisplayedInTable(firstName, count));
        campaignFiltersStep.deleteContactFromDb(firstName);
    }

    private void goToFilterTab(){
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        campaignFiltersStep.openFiltersTab();
    }
}
