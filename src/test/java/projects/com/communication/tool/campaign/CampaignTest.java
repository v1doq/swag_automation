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

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;

@Feature("Campaign")
@Story("Functional tests for campaign creation")
public class CampaignTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private String campaignName = randomAlphabetic(5);
    private String companyName = randomAlphabetic(5);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        campaignStep = new CampaignStep(driver);
        campaignStep.createCampaignInDB(campaignName, companyName);
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create new company and add campaign to it")
    public void createNewCompanyAndAddCampaignToIt() {
        String campaignName = randomAlphabetic(5);
        String companyName = randomAlphabetic(5);
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCompanyAndCampaignInList(companyName, campaignName));
        assertTrue(campaignStep.isCampaignAssignToCompany(companyName, campaignName));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Add campaign to existing company")
    public void addCampaignToExistingCompany() {
        String campaignName = randomAlphabetic(5);
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCompanyAndCampaignInList(companyName, campaignName));
        assertTrue(campaignStep.isCampaignAssignToCompany(companyName, campaignName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Check validation messages for empty mandatory fields")
    public void checkValidationMessagesForAddNewCampaignFields() {
        campaignStep.openCampaignPage();
        campaignStep.openCampaignPopUp();
        campaignStep.submitCampaign();

        assertTrue(campaignStep.isCampaignErrorDisplayed());
        assertTrue(campaignStep.isCompanyErrorDisplayed());
    }
}
