package projects.com.communication.tool.contacts;

import common.SuiteTestCT;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.campaign.ContactsStep;
import projects.com.communication.tool.steps.contacts.FilterStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;

@Feature("Filters")
@Story("Functional tests for filters selection")
public class FilterTest extends SuiteTestCT {

    private FilterStep filterStep;
    private ContactsStep contactsStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        filterStep = new FilterStep(driver);
        contactsStep = new ContactsStep(driver);
        loginWithToken();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = {"smoke test"}, description = "Apply all filters and check count of records in counter")
    public void selectFilterAndVerifyContactsInCounter() {
        String firstName = randomAlphabetic(5);
        contactsStep.insertContactToDb(firstName);

        filterStep.openFiltersPage();
        int count = filterStep.setFiltersByFirstName(firstName);

        assertEquals(filterStep.getRecordsCounter(), String.valueOf(count));
        contactsStep.deleteContactFromDb(firstName);
    }
}
