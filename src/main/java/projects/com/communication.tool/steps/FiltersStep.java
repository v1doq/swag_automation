package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.FiltersComponent;
import settings.SQLConnector;

import static common.ConciseApi.select;
import static org.openqa.selenium.By.className;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class FiltersStep {

    private FiltersComponent component;
    public static String EQUAL = "Equal";
    public static String FIRST_NAME = "First Name";

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

    @Step("Verify count of records in counter")
    public void waitForRecordsResult(int records) {
        component.waitForPartOfText(className("filter-count"), String.valueOf(records));
    }

    @Step("Verify count of records in counter")
    public String getRecordsCounter() {
        String text = " record(s) satisfy criteria";
        return component.getCounterValue().getText().replace(text, "");
    }

    @Step("Get contact count in the database")
    public int getContactsCountInDB(String name, String column){
        LOG.info("Get contact count in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT COUNT(*) AS total FROM CommunicationTool.dbo.Contact WHERE " + column + "='" + name + "'";
        return connector.getIntValueInDB(query, "total");
    }
}
