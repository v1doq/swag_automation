package projects.com.communication.tool;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.BeforeSuite;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.CampaignStep;
import projects.com.communication.tool.steps.LoginStepCT;
import settings.LocalStorage;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;

@Feature("Campaign")
@Story("Functional tests for campaign")
public class CampaignTest extends BaseTest {

    private CampaignStep campaignStep;
    private LocalStorage storage;
    private LoginStepCT loginStep;
    private String value;
    private String key = "default_auth_token";

    @BeforeSuite(description = "Get authorization token", alwaysRun = true)
    public void getToken() {
        openBrowser();
        storage = new LocalStorage(driver);
        loginStep = new LoginStepCT(driver);
        loginStep.authorization();
        value = storage.getValueFromLocalStorage(key);
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void init() {
        campaignStep = new CampaignStep(driver);
        loginStep = new LoginStepCT(driver);
        storage = new LocalStorage(driver);
        loginStep.openLandingPage();
        storage.setItemInLocalStorage(key, value);
        addCookies();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create new company and campaign")
    public void createNewCampaign() {
        campaignStep.openCampaignPage();
        String campaignName = randomAlphabetic(5);
        String companyName = randomAlphabetic(5);
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCompanyDisplayedInList(companyName));
        assertTrue(campaignStep.isCampaignDisplayedInList(campaignName));
    }
}
