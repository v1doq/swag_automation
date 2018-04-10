package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.FiltersComponent;
import settings.SQLConnector;

import static common.ConciseApi.select;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class FiltersStep {

    private FiltersComponent component;
    public static final String EQUAL_CRITERION = "Equal";
    public static final String START_WITH = "Starts With";
    public static final String FIRST_NAME = "First Name";
    public static final String STREET = "Street";
    public static final String ID = "Id";
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
        component.getValueInput().sendKeys(value);
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
        String query = QUERY + column + criterion + "'" + value + "'";
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
}
