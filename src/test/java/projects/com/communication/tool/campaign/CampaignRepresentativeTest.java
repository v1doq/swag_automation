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
import projects.com.communication.tool.steps.campaign.RepresentativeStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.*;

@Feature("Campaign's representative")
@Story("Functional tests for representative tab in campaign")
public class CampaignRepresentativeTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private RepresentativeStep repsStep;
    private String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        campaignStep = new CampaignStep(driver);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        repsStep = new RepresentativeStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create representative")
    public void createNewRepresentative() {
        String fromName = randomAlphabetic(5);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        repsStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, fromName);

        assertTrue(repsStep.isFromNameDisplayedInRepsCards(fromName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity positive reps", dataProvider = "Valid", dataProviderClass = RepresentativeStep.class,
            timeOut = 120000, description = "Create gateway with valid credential")
    public void createGatewaysWithValidCredential(String smtp, String imap, String email, String pass) {
        String fromName = randomAlphabetic(5);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        repsStep.fillRepsFields(email, fromName);
        repsStep.fillGatewayFields(email, pass, smtp, imap);
        repsStep.saveRepresentative();

        assertTrue(repsStep.isFromNameDisplayedInRepsCards(fromName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity negative reps", dataProvider = "Invalid", dataProviderClass = RepresentativeStep.class,
            timeOut = 120000, description = "Try to create gateway with invalid credential")
    public void tryToCreateGatewayWithInvalidCredential(String smtp, String imap, String email, String pass) {
        String fromName = randomAlphabetic(5);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        repsStep.fillRepsFields(email, fromName);
        repsStep.fillGatewayFields(email, pass, smtp, imap);
        repsStep.saveRepresentative();

        assertFalse(repsStep.isFromNameDisplayedInRepsCards(fromName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity positive reps", description = "Update representative")
    public void updateRepresentative() {
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        repsStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, randomAlphabetic(MIN_FROM_NAME_LENGTH));

        String fromName = randomAlphabetic(5);
        repsStep.updateRepresentative(fromName);

        assertTrue(repsStep.isFromNameEqualsDbValue(fromName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity positive reps", timeOut = 120000, description = "Delete representative")
    public void deleteRepresentative() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        repsStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, randomAlphabetic(MIN_FROM_NAME_LENGTH));

        repsStep.deleteRepresentative();

        assertTrue(repsStep.isEmptyRepsFormDisplayed());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity positive reps", timeOut = 120000,
            description = "Check created placeholder in all campaign's representative")
    public void checkCreatedPlaceholderInSecondCampaignsReps() {
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        String fromName = randomAlphabetic(5);
        String key = randomAlphabetic(1).toUpperCase() + randomAlphabetic(5).toLowerCase();
        String value = randomAlphabetic(5);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(5));
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        repsStep.createRepsWithPlaceholder(GATEWAY_OUTLOOK_EMAIL, fromName, key, value);

        assertTrue(repsStep.isPlaceholderDisplayedInList(key));
    }
}
