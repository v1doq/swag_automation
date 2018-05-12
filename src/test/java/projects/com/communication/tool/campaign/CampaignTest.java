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
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;

@Feature("Campaign")
@Story("Functional tests for campaign creation")
public class CampaignTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private String companyName = randomAlphabetic(MIN_COMPANY_NAME_LENGTH);
    private String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);

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
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        String companyName = randomAlphabetic(MIN_COMPANY_NAME_LENGTH);
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCompanyAndCampaignInList(companyName, campaignName));
        assertTrue(campaignStep.isCampaignAssignToCompany(companyName, campaignName));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Add campaign to existing company")
    public void addCampaignToExistingCompany() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCompanyAndCampaignInList(companyName, campaignName));
        assertTrue(campaignStep.isCampaignAssignToCompany(companyName, campaignName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Update campaign name and description")
    public void updateCampaign() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.createCampaignInDB(campaignName, companyName);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        String newCampaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        String campaignDesc = randomAlphabetic(MIN_CAMPAIGN_DESC_LENGTH);
        campaignStep.updateCampaign(newCampaignName, campaignDesc);

        assertEquals(campaignStep.getCampaignNameInPreview(), newCampaignName);
        assertEquals(campaignStep.getCampaignDescInPreview(), campaignDesc);
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "sanity campaign", description = "Check validation messages for empty mandatory fields")
    public void checkValidationMessagesForAddNewCampaignFields() {
        campaignStep.openCampaignPage();
        campaignStep.openCampaignPopUp();
        campaignStep.submitCampaign();

        assertTrue(campaignStep.isCampaignErrorDisplayed());
        assertTrue(campaignStep.isCompanyErrorDisplayed());
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "sanity campaign", description = "Try to create campaign with existing name")
    public void tryToCreateCampaignWithExistingName() {
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCampaignServerErrorDisplayedInPopUp());
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "sanity campaign", description = "Try to update campaign name with existing name")
    public void tryToUpdateCampaignNameWithExistingName() {
        String newCampaign = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.openCampaignPage();
        campaignStep.createCampaign(newCampaign, companyName);
        campaignStep.selectCampaignInList(newCampaign);

        campaignStep.updateCampaignName(campaignName);

        assertTrue(campaignStep.isCampaignServerErrorDisplayedInPreview());
    }
}
