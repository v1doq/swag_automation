package projects.com.communication.tool.steps.contacts;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.contacts.FilterComponent;
import settings.SQLConnector;

import static settings.SQLConnector.NOT_EQUAL;
import static settings.TestConfig.getProperty;

public class FiltersStep {

    private FilterComponent component;
    public static final String ENTITY_USER = "User";
    public static final String EQUAL_CRITERION = "Equal";
    public static final String FIRST_NAME_FILTER = "First Name";
    private static final String QUERY = "SELECT COUNT(*) AS total FROM CommunicationTool.dbo.Contact WHERE ";

    public FiltersStep(WebDriver driver) {
        this.component = new FilterComponent(driver);
    }

    @Step("Open filters page")
    public void openFiltersPage() {
        component.open(getProperty("communication.tool.url") + "contacts/filters/");
    }

    @Step("Apply all filters")
    public void applyAllFilters(String entity, String column, String criterion, String value) {
        component.getEntityInput().sendKeys(entity);
        component.waitForText(component.getSearchResult(), entity);
        component.getEntityInput().sendKeys(Keys.ENTER);
        component.getFieldInput().sendKeys(column);
        component.waitForText(component.getSearchResult(), column);
        component.getFieldInput().sendKeys(Keys.ENTER);
        component.getCriterionInput().sendKeys(criterion);
        component.waitForText(component.getSearchResult(), criterion);
        component.getCriterionInput().sendKeys(Keys.ENTER);
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
        if (!criterion.equals(NOT_EQUAL)) {
            query = QUERY + column + criterion + "'" + value + "'";
        } else {
            query = QUERY + column + criterion + "'" + value + "'OR " + column + " is NULL";
        }
        SQLConnector connector = new SQLConnector();
        return connector.getIntValueInDB(query, "total");
    }
}
