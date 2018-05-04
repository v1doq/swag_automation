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
import projects.com.communication.tool.steps.campaign.TemplateStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;

@Feature("Campaign")
@Story("Functional tests for template tab in campaign")
public class TemplateTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private TemplateStep templateStep;
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
        templateStep = new TemplateStep(driver);
        loginWithToken();
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
}
