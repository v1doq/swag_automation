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
import static org.testng.Assert.*;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;

@Feature("Campaign's CRUD")
@Story("Functional tests for campaign CRUD")
public class CampaignCrudTest extends SuiteTestCT {

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
        campaignStep.openCampaignPage();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "sanity campaign"}, description = "Create new company and add campaign to it")
    public void createNewCompanyAndAddCampaignToIt() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        String companyName = randomAlphabetic(MIN_COMPANY_NAME_LENGTH);

        campaignStep.createCampaign(campaignName, companyName);

        String campaignNameInDB = campaignStep.getCampaignNameByCompanyInDb(campaignName, companyName);
        assertEquals(campaignNameInDB, campaignName);
        assertTrue(campaignStep.isCompanyAndCampaignInList(companyName, campaignName));
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "sanity campaign"}, description = "Add campaign to existing company")
    public void addCampaignToExistingCompany() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.createCampaign(campaignName, companyName);
        String campaignNameInDB = campaignStep.getCampaignNameByCompanyInDb(campaignName, companyName);

        assertEquals(campaignNameInDB, campaignName);
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
        campaignStep.updateCampaignName(newCampaignName);
        campaignStep.updateCampaignDesc(campaignDesc);

        assertEquals(campaignStep.getCampaignNameInPreview(), newCampaignName);
        assertEquals(campaignStep.getCampaignDescInPreview(), campaignDesc);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity campaign", description = "Delete campaign without messages")
    public void deleteCampaign() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.createCampaignInDB(campaignName, companyName);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        campaignStep.deleteCampaign();

        String campaignNameInDB = campaignStep.getCampaignNameByCompanyInDb(campaignName, companyName);
        assertNull(campaignNameInDB);
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "sanity campaign", description = "Check validation messages for empty mandatory fields")
    public void checkValidationMessagesInCampaignPopUp() {
        campaignStep.createCampaign("", "");

        assertTrue(campaignStep.isValidationMessagesDisplayed());
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "sanity campaign", description = "Try to update campaign name with existing name")
    public void tryToUpdateCampaignNameWithExistingName() {
        String newCampaign = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.createCampaign(newCampaign, companyName);
        campaignStep.selectCampaignInList(newCampaign);

        campaignStep.updateCampaignName(campaignName);

        assertTrue(campaignStep.isServerErrorDisplayed(DUPLICATE_CAMPAIGN_ERROR));
    }
}
