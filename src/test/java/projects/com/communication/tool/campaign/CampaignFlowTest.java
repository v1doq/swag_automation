package projects.com.communication.tool.campaign;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.CampaignStep;
import projects.com.communication.tool.steps.campaign.FlowStep;
import projects.com.communication.tool.steps.campaign.TemplateStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertNull;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.CampaignStep.MIN_CAMPAIGN_NAME_LENGTH;
import static projects.com.communication.tool.steps.campaign.CampaignStep.MIN_COMPANY_NAME_LENGTH;
import static projects.com.communication.tool.steps.campaign.FlowStep.WORK_EMAIL;

@Feature("Campaign's flow")
@Story("Functional tests for flow tab in campaign")
public class CampaignFlowTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private TemplateStep templateStep;
    private FlowStep flowStep;

    @BeforeMethod(description = "Preconditions", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        templateStep = new TemplateStep(driver);
        flowStep = new FlowStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "sanity campaign"}, description = "Create flow with template")
    public void createFlowWithTemplate() {
        String subj = randomAlphabetic(10);
        String body = randomAlphabetic(10);
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
        goToFlowTab(campaignName);

        flowStep.createFlow(WORK_EMAIL);
        assertTrue(flowStep.isTypeAppliedToFlow(WORK_EMAIL));
        templateStep.createTemplate(subj, body);
        flowStep.saveFlow();

        String subjInDb = templateStep.getTemplateSubjInDB(campaignName);
        assertEquals(subjInDb, subj);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"sanity campaign"}, description = "Create flow without template")
    public void createFlowWithoutTemplate() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
        goToFlowTab(campaignName);

        flowStep.createFlow(WORK_EMAIL);
        flowStep.saveFlow();

        assertNull(templateStep.getTemplateSubjInDB(campaignName));
    }

    private void goToFlowTab(String campaignName){
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        flowStep.openFlowTab();
    }
}
