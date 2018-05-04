package projects.com.communication.tool.campaign;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.CampaignStep;
import projects.com.communication.tool.steps.campaign.RepresentativeStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.RepresentativeStep.*;

@Feature("Campaign")
@Story("Functional tests for representative tab in campaign")
public class RepresentativeTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private RepresentativeStep repsStep;
    private String campaignName = randomAlphabetic(5);

    @BeforeClass(description = "Create new campaign", alwaysRun = true)
    public void createCampaign() {
        campaignStep = new CampaignStep(driver);
        campaignStep.createCampaignInDB(campaignName, randomAlphabetic(5));
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
        String fromName = randomAlphabetic(MIN_FROM_NAME_LENGTH);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        repsStep.createRepresentative(GATEWAY_OUTLOOK_EMAIL, fromName);
        repsStep.saveRepresentative();

        assertTrue(repsStep.isRepresentativeCreated());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "sanity reps", dataProvider = "Reps", description = "Create valid gateways")
    public void createValidGateways(String smtp, String imap, String email, String pass) {
        String fromName = randomAlphabetic(MIN_FROM_NAME_LENGTH);
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);

        repsStep.fillRepsFields(email, fromName);
        repsStep.fillGatewayFields(email, pass, smtp, imap);
        repsStep.saveRepresentative();

        assertTrue(repsStep.isRepresentativeCreated());
    }

    @DataProvider(name = "Reps")
    public Object[][] validCredential() {
        String outlookEmail = GATEWAY_OUTLOOK_EMAIL;
        String outlookPass = GATEWAY_OUTLOOK_PASS;
        String gmailEmail = GATEWAY_GMAIL_EMAIL;
        String gmailPass = GATEWAY_GMAIL_PASS;
        return new Object[][]{
                {"smtp.outlook.com:25", "imap-mail.outlook.com:993", outlookEmail, outlookPass},
                {"smtp.outlook.com:587", "imap-mail.outlook.com:993", outlookEmail, outlookPass},
                {"smtp-mail.outlook.com:587", "outlook.office365.com", outlookEmail, outlookPass},
                {"smtp.office365.com:587", "imap.outlook.com:993", outlookEmail, outlookPass},
                {"smtp.gmail.com:587", "imap.gmail.com:993", gmailEmail, gmailPass},
                {"smtp.gmail.com:25", "imap.gmail.com:993", gmailEmail, gmailPass}
        };
    }
}
