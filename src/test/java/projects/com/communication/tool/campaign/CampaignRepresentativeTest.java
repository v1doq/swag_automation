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
import projects.com.communication.tool.steps.campaign.FlowStep;
import projects.com.communication.tool.steps.campaign.RepresentativeStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;
import static projects.com.communication.tool.steps.campaign.FlowStep.WORK_EMAIL_CHANNEL;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.*;

@Feature("Campaign's representative")
@Story("Functional tests for representative tab in campaign")
public class CampaignRepresentativeTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private RepresentativeStep repsStep;
    private FlowStep flowStep;
    private String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        campaignStep = new CampaignStep(driver);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
    }

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        campaignStep = new CampaignStep(driver);
        flowStep = new FlowStep(driver);
        repsStep = new RepresentativeStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create representative")
    public void createNewRepresentative() {
        String fromName = randomAlphabetic(5);
        selectCampaign(campaignName);

        repsStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, fromName);

        assertTrue(repsStep.isFromNameDisplayedInRepsCards(fromName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity positive reps", dataProvider = "Valid", dataProviderClass = RepresentativeStep.class,
            description = "Create gateway with valid credential")
    public void createGatewaysWithValidCredential(String smtp, String imap, String email, String pass) {
        String fromName = randomAlphabetic(5);
        selectCampaign(campaignName);

        repsStep.fillRepsFields(email, fromName);
        repsStep.fillGatewayFields(email, pass, smtp, imap);
        repsStep.saveRepresentative();

        assertTrue(repsStep.isFromNameDisplayedInRepsCards(fromName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity negative reps", dataProvider = "Invalid", dataProviderClass = RepresentativeStep.class,
            description = "Try to create gateway with invalid credential")
    public void tryToCreateGatewayWithInvalidCredential(String smtp, String imap, String email, String pass) {
        String fromName = randomAlphabetic(5);
        selectCampaign(campaignName);

        repsStep.fillRepsFields(email, fromName);
        repsStep.fillGatewayFields(email, pass, smtp, imap);
        repsStep.saveRepresentative();

        assertFalse(repsStep.isFromNameDisplayedInRepsCards(fromName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity positive reps", description = "Update representative")
    public void updateRepresentative() {
        createAndSelectCampaign();
        repsStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, randomAlphabetic(MIN_FROM_NAME_LENGTH));

        String fromName = randomAlphabetic(5);
        repsStep.updateRepresentative(fromName);

        assertTrue(repsStep.isFromNameEqualsDbValue(fromName));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity positive reps", description = "Delete representative")
    public void deleteRepresentative() {
        createAndSelectCampaign();
        repsStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, randomAlphabetic(MIN_FROM_NAME_LENGTH));

        repsStep.deleteRepresentative();

        assertTrue(repsStep.isEmptyRepsFormDisplayed());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity positive reps", description = "Check created placeholder in all campaign's representative")
    public void checkCreatedPlaceholderInSecondCampaignsReps() {
        String key = randomAlphabetic(1).toUpperCase() + randomAlphabetic(5).toLowerCase();
        String value = randomAlphabetic(5);
        createAndSelectCampaign();

        repsStep.createRepsWithPlaceholder(GATEWAY_OUTLOOK_EMAIL, randomAlphabetic(5), key, value);

        assertTrue(repsStep.isPlaceholderDisplayedInList(key));
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "sanity negative reps", description = "Check validation messages")
    public void checkErrorValidationMessages() {
        selectCampaign(campaignName);

        repsStep.fillRepsFields(" ", " ");
        repsStep.fillGatewayFields(" ", " ", " ", " ");
        repsStep.clickSaveButton();

        assertTrue(repsStep.isValidationMessagesDisplayed(REQUIRED_FIELDS_ERRORS));
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "sanity negative reps", description = "Check validation messages")
    public void tryToStartCampaignWithoutRepresentative() {
        createAndSelectCampaign();
        flowStep.saveFlowWithoutTemplate(WORK_EMAIL_CHANNEL);

        campaignStep.activateCommunication();

        assertTrue(campaignStep.isServerErrorDisplayed(COMMUNICATION_START_ERROR));
    }

    private void selectCampaign(String campaignName){
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
    }

    private void createAndSelectCampaign(){
        String campaignName = randomAlphabetic(MIN_CAMPAIGN_NAME_LENGTH);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(MIN_COMPANY_NAME_LENGTH));
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
    }
}
