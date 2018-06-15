package projects.com.communication.tool.contacts;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.CampaignFiltersStep;
import projects.com.communication.tool.steps.contacts.FiltersStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static projects.com.communication.tool.steps.contacts.FiltersStep.*;
import static settings.SQLConnector.EQUAL;

@Feature("Filters")
@Story("Functional tests for filters selection")
public class FilterTest extends SuiteTestCT {

    private FiltersStep filtersStep;
    private CampaignFiltersStep campaignFiltersStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        filtersStep = new FiltersStep(driver);
        campaignFiltersStep = new CampaignFiltersStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test"}, description = "Apply all filters and check count of records in counter")
    public void selectFilterAndVerifyContactsInCounter() {
        String firstName = randomAlphabetic(5);
        campaignFiltersStep.insertContactToDb(firstName);

        filtersStep.openFiltersPage();
        filtersStep.applyAllFilters(ENTITY_USER, FIRST_NAME_FILTER, EQUAL_CRITERION, firstName);
        int count = filtersStep.getValueByCriterion("FirstName", EQUAL, firstName);
        filtersStep.waitForRecordsResult(count);

        assertEquals(filtersStep.getRecordsCounter(), String.valueOf(count));
        campaignFiltersStep.deleteContactFromDb(firstName);
    }
}
