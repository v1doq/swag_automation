package projects.com.communication.tool;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.FiltersStep;

import static org.testng.Assert.assertEquals;
import static projects.com.communication.tool.steps.FiltersStep.*;
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
    @Test(groups = "smoke test", description = "Apply all filters and check count of records in counter")
    public void selectFilterAndVerifyContactsInCounter() {
        String value = "Norman";
        filtersStep.openContactsPage();
        filtersStep.applyAllFilters(FIRST_NAME_FILTER, EQUAL_CRITERION, value);
        int count = filtersStep.getValueByCriterion("FirstName", EQUAL, value);
        filtersStep.waitForRecordsResult(count);

        assertEquals(filtersStep.getRecordsCounter(), String.valueOf(count));
    }

    @Ignore
    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "filters", dataProvider = "Filters", dataProviderClass = FiltersStep.class
            , description = "Check count of records with filter by names")
    public void filteringByName(String column, String criterion, String value, String columnDb, String criterionDB,
                                     String valueDB) {
        filtersStep.openContactsPage();
        filtersStep.applyAllFilters(column, criterion, value);
        int count = filtersStep.getValueByCriterion(columnDb, criterionDB, valueDB);
        filtersStep.waitForRecordsResult(count);

        assertEquals(filtersStep.getRecordsCounter(), String.valueOf(count));
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "filters", description = "Check count of records with filter by ID")
    public void checkCountOfRecordsWithFilterById(){
        String value = filtersStep.getValueInContactTableDB(ID_FILTER);
        filtersStep.openContactsPage();
        filtersStep.applyAllFilters(ID_FILTER, EQUAL_CRITERION, value);
        filtersStep.waitForRecordsResult(1);

        assertEquals(filtersStep.getRecordsCounter(), "1");
    }
}
