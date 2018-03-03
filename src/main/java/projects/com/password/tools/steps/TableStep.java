package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.TableComponent;

import static common.ConciseApi.clearAndType;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;

public class TableStep {

    private TableComponent component;

    public TableStep(WebDriver driver) {
        this.component = new TableComponent(driver);
    }

    @Step("Search value in table")
    public void searchInTable(String query){
        clearAndType(component.getSearchInput(), query);
        component.waitForPartOfText(cssSelector("tbody > tr:nth-child(1)"), query);
    }

    @Step("Delete value in table")
    public void deleteDataInTable(){
        component.getDeleteButton().click();
        component.waitForPartOfText(cssSelector(".card__text"), "Are you sure to delete");
        component.jsClick(component.getYesButton());
        component.assertThat(invisibilityOfElementLocated(cssSelector(".card__text")));
    }

    @Step("Checking the presence of a value in the table")
    public boolean isValueDisplayInTable(String value){
        return component.isTextDisplayed(value, "td");
    }
}
