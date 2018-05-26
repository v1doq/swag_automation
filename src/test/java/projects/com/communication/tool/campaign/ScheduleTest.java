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
import projects.com.communication.tool.steps.campaign.ScheduleStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;

@Feature("Campaign")
@Story("Functional tests for schedule tab in campaign")
public class ScheduleTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private ScheduleStep scheduleStep;
    private String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
    private String companyName = randomAlphabetic(MIN_COMPANY_NAME_LENGTH);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        campaignStep = new CampaignStep(driver);
        campaignStep.createCampaignInDB(campaignName, companyName);
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        scheduleStep = new ScheduleStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "sanity campaign"}, description = "Update schedule parameters")
    public void updateScheduleParametersAndCheckItInDb() {
        String interval = "1";
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        scheduleStep.openScheduleTab();
        scheduleStep.updateSchedule(interval);
        String intervalInDb = scheduleStep.getScheduleIntervalInDB(campaignName);

        assertEquals(intervalInDb, interval);
    }
}
