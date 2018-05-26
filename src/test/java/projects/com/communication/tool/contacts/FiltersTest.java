package projects.com.communication.tool.contacts;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.contacts.FiltersStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static projects.com.communication.tool.steps.contacts.FiltersStep.*;
import static settings.SQLConnector.EQUAL;

@Feature("Filters")
@Story("Functional tests for filters")
public class FiltersTest extends SuiteTestCT {

    private FiltersStep filtersStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        filtersStep = new FiltersStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test", "filters"}, description = "Apply all filters and check count of records in counter")
    public void selectFilterAndVerifyContactsInCounter() {
        String firstName = randomAlphabetic(5);
        String email = randomAlphabetic(5) + "@i.ua";
        filtersStep.insertContactToDb(firstName, email);

        filtersStep.openContactsPage();
        filtersStep.applyAllFilters(ENTITY_USER, FIRST_NAME_FILTER, EQUAL_CRITERION, firstName);
        int count = filtersStep.getValueByCriterion("FirstName", EQUAL, firstName);
        filtersStep.waitForRecordsResult(count);

        assertEquals(filtersStep.getRecordsCounter(), String.valueOf(count));
        filtersStep.deleteContactFromDb(firstName, email);
    }

    @Ignore
    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "filters", dataProvider = "Filters", dataProviderClass = FiltersStep.class
            , description = "Check count of records with filter by names")
    public void filteringByName(String column, String criterion, String value, String columnDb, String criterionDB,
                                     String valueDB) {
        filtersStep.openContactsPage();
        filtersStep.applyAllFilters(ENTITY_USER, column, criterion, value);
        int count = filtersStep.getValueByCriterion(columnDb, criterionDB, valueDB);
        filtersStep.waitForRecordsResult(count);

        assertEquals(filtersStep.getRecordsCounter(), String.valueOf(count));
    }
}
