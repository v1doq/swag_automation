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
import projects.com.communication.tool.steps.GatewayStep;
import projects.com.communication.tool.steps.LoginStepCT;
import settings.LocalStorage;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.GatewayStep.MIN_FROM_NAME_LENGTH;

@Feature("Campaign")
@Story("Functional tests for campaign")
public class CampaignTest extends BaseTest {

    private CampaignStep campaignStep;
    private GatewayStep gatewayStep;
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
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        gatewayStep = new GatewayStep(driver);
        loginStep = new LoginStepCT(driver);
        storage = new LocalStorage(driver);
        loginStep.openLandingPage();
        storage.setItemInLocalStorage(key, value);
        addCookies();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create new company and add campaign to it")
    public void createNewCompanyAndAddCampaignToIt() {
        campaignStep.openCampaignPage();
        String campaignName = randomAlphabetic(5);
        String companyName = randomAlphabetic(5);
        campaignStep.createCampaign(campaignName, companyName);

        assertTrue(campaignStep.isCompanyDisplayedInList(companyName));
        assertTrue(campaignStep.isCampaignDisplayedInList(campaignName));
        assertTrue(campaignStep.isCampaignAssignToCompany(companyName, campaignName));

        campaignStep.deleteCompanyInDB(companyName, campaignName);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create new gateway")
    public void createNewGateway() {
        String campaignName = randomAlphabetic(5);
        String companyName = randomAlphabetic(5);
        String fromName = randomAlphabetic(MIN_FROM_NAME_LENGTH);
        campaignStep.createCampaignInDB(campaignName, companyName);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        gatewayStep.openGatewayTab();
        gatewayStep.createGateway(fromName);
        assertTrue(gatewayStep.isFromNameDisplayedInGateway(fromName));

        gatewayStep.deleteGatewayInDB(fromName);
        campaignStep.deleteCompanyInDB(companyName, campaignName);
    }
}
