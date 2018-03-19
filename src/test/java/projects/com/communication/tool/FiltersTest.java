package projects.com.communication.tool;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.FiltersStep;
import projects.com.communication.tool.steps.LoginStepCT;

import static org.testng.Assert.assertEquals;
import static projects.com.communication.tool.steps.FiltersStep.*;

@Feature("Filters")
@Story("Functional tests for filters")
public class FiltersTest extends BaseTest {

    private FiltersStep filtersStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        filtersStep = new FiltersStep(driver);
        LoginStepCT loginStep = new LoginStepCT(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Apply all filters and check count of records in counter")
    public void selectFilterAndVerifyContactsInCounter() {
        String value = "rebecca";
        filtersStep.openContactsPage();
        filtersStep.applyAllFilters(FIRST_NAME, EQUAL, value);
        int count = filtersStep.getContactsCountInDB(value, "FirstName");
        filtersStep.waitForRecordsResult(count);

        assertEquals(String.valueOf(count), filtersStep.getRecordsCounter());
    }
}
