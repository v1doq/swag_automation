package projects.com.communication.tool.steps.contacts;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.testng.annotations.DataProvider;
import projects.com.communication.tool.components.contacts.FiltersComponent;
import settings.SQLConnector;

import static common.ConciseApi.select;
import static settings.SQLConnector.*;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class FiltersStep {

    private FiltersComponent component;
    private static final String CONTAINS_CRITERION = "Contains";
    private static final String START_WITH_CRITERION = "Starts With";
    private static final String ENDS_WITH_CRITERION = "Ends With";
    public static final String EQUAL_CRITERION = "Equal";
    private static final String NOT_EQUAL_CRITERION = "Not Equal";
    private static final String VALUE = "t";

    public static final String FIRST_NAME_FILTER = "First Name";
    public static final String POSITION_FILTER = "Position";
    public static final String ID_FILTER = "Id";
    private static final String QUERY = "SELECT COUNT(*) AS total FROM CommunicationTool.dbo.Contact WHERE ";

    public FiltersStep(WebDriver driver) {
        this.component = new FiltersComponent(driver);
    }

    @Step("Open contacts page")
    public void openContactsPage() {
        component.open(getProperty("communication.tool.url") + "contacts/filters/");
    }

    @Step("Apply all filters")
    public void applyAllFilters(String column, String criterion, String value) {
        select(component.getFieldSelect(), column);
        select(component.getCriterionSelect(), criterion);
        component.jsClearAndSendKeys(component.getValueInput(), value);
    }

    public void waitForRecordsResult(int records) {
        component.waitForPartOfText(component.getCounter(), String.valueOf(records));
    }

    @Step("Verify count of records in counter")
    public String getRecordsCounter() {
        String text = " record(s) satisfy criteria";
        return component.getCounterValue().getText().replace(text, "");
    }

    @Step("Get contact's count in the database")
    public int getValueByCriterion(String column, String criterion, String value) {
        String query;
        if (!criterion.equals(NOT_EQUAL)){
            query = QUERY + column + criterion + "'" + value + "'";
        } else {
            query = QUERY + column + criterion + "'" + value + "'OR " + column + " is NULL";
        }
        SQLConnector connector = new SQLConnector();
        return connector.getIntValueInDB(query, "total");
    }

    @Step("Get value in contact table in the database")
    public String getValueInContactTableDB(String column){
        LOG.info("Get value in contact table in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT " + column + " FROM CommunicationTool.dbo.Contact";
        return connector.getStringValueInDB(query, column);
    }

    @DataProvider(name = "Filters")
    public static Object[][] credentials() {
        return new Object[][]{
                {"First Name", CONTAINS_CRITERION, VALUE, "FirstName", LIKE, "%" + VALUE + "%"},
                {"First Name", START_WITH_CRITERION, VALUE, "FirstName", LIKE, VALUE + "%"},
                {"First Name", ENDS_WITH_CRITERION, VALUE, "FirstName", LIKE, "%" + VALUE},
                {"First Name", NOT_EQUAL_CRITERION, VALUE, "FirstName", NOT_EQUAL, VALUE},

                {"Middle Name", CONTAINS_CRITERION, VALUE, "MiddleName", LIKE, "%" + VALUE + "%"},
                {"Middle Name", START_WITH_CRITERION, VALUE, "MiddleName", LIKE, VALUE + "%"},
                {"Middle Name", ENDS_WITH_CRITERION, VALUE, "MiddleName", LIKE, "%" + VALUE},
                {"Middle Name", EQUAL_CRITERION, VALUE, "MiddleName", EQUAL, VALUE},
                {"Middle Name", NOT_EQUAL_CRITERION, VALUE, "MiddleName", NOT_EQUAL, VALUE},

                {"Last Name", CONTAINS_CRITERION, VALUE, "LastName", LIKE, "%" + VALUE + "%"},
                {"Last Name", START_WITH_CRITERION, "test", "LastName", LIKE, "test%"},
                {"Last Name", ENDS_WITH_CRITERION, VALUE, "LastName", LIKE, "%" + VALUE},
                {"Last Name", EQUAL_CRITERION, VALUE, "LastName", EQUAL, VALUE},
                {"Last Name", NOT_EQUAL_CRITERION, VALUE, "LastName", NOT_EQUAL, VALUE},

                {"Full Name", CONTAINS_CRITERION, VALUE, "FullName", LIKE, "%" + VALUE + "%"},
                {"Full Name", START_WITH_CRITERION, "test", "FullName", LIKE, "test%"},
                {"Full Name", ENDS_WITH_CRITERION, VALUE, "FullName", LIKE, "%" + VALUE},
                {"Full Name", EQUAL_CRITERION, "david", "FullName", EQUAL, "david"},
                {"Full Name", NOT_EQUAL_CRITERION, VALUE, "FullName", NOT_EQUAL, VALUE},
        };
    }
}