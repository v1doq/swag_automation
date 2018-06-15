package projects.com.communication.tool.steps.contacts;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.contacts.FilterComponent;
import settings.SQLConnector;

import static settings.SQLConnector.EQUAL;
import static settings.SQLConnector.NOT_EQUAL;
import static settings.TestConfig.getProperty;

public class FilterStep {

    private FilterComponent component;
    private static final String QUERY = "SELECT COUNT(*) AS total FROM CommunicationTool.dbo.Contact WHERE ";

    public FilterStep(WebDriver driver) {
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

    @Step("Change value only in filter")
    public int changeValueInFilter(String value) {
        component.jsClearAndSendKeys(component.getValueInput(), value);
        int count = getValueByCriterion("FirstName", EQUAL, value);
        waitForRecordsResult(count);
        return count;
    }

    public int setFiltersByFirstName(String firstName){
        applyAllFilters("User", "First Name", "Equal", firstName);
        int count = getValueByCriterion("FirstName", EQUAL, firstName);
        waitForRecordsResult(count);
        return count;
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

    private void waitForRecordsResult(int records) {
        component.waitForPartOfText(component.getCounter(), String.valueOf(records));
    }
}
