package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.TableComponent;

public class TableStep {

    private TableComponent component;

    public TableStep(WebDriver driver) {
        this.component = new TableComponent(driver);
    }

    @Step("Search in table")
    public void searchInTable(String query){
        component.getSearchInput().sendKeys(query);
    }

    @Step("Delete in table")
    public void deleteInTable(String value){
        searchInTable(value);
        component.getDeleteButton().click();
    }
}
