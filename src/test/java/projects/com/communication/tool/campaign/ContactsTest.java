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
import projects.com.communication.tool.steps.campaign.ContactsStep;
import projects.com.communication.tool.steps.contacts.FiltersStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.campaign.CampaignStep.*;
import static projects.com.communication.tool.steps.contacts.FiltersStep.*;
import static settings.SQLConnector.EQUAL;

@Feature("Campaign")
@Story("Functional tests for contacts tab in campaign")
public class ContactsTest extends SuiteTestCT {

    private CampaignStep campaignStep;
    private ContactsStep contactsStep;
    private FiltersStep filtersStep;
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
        contactsStep = new ContactsStep(driver);
        filtersStep = new FiltersStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Add contacts to campaign")
    public void addContactsToCampaign() {
        String value = "Regulatory Affairs Associate";
        campaignStep.openCampaignPage();
        campaignStep.selectCampaignInList(campaignName);
        contactsStep.openContactsTab();

        contactsStep.openContactsPopUp();
        filtersStep.applyAllFilters(POSITION_FILTER, EQUAL_CRITERION, value);
        int count = filtersStep.getValueByCriterion("[Position]", EQUAL, value);

        filtersStep.waitForRecordsResult(count);
        assertEquals(filtersStep.getRecordsCounter(), String.valueOf(count));

        contactsStep.saveContactsInCampaign();
        assertTrue(contactsStep.isContactsAddedToCampaign("Jaclyn"));
    }
}
