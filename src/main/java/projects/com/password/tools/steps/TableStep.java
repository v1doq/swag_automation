package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.TableComponent;

import static common.ConciseApi.sleep;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class TableStep {

    private TableComponent component;

    public TableStep(WebDriver driver) {
        this.component = new TableComponent(driver);
    }

    @Step("Search value in table")
    public void searchInTable(String query){
        component.clearAndSendKeys(component.getSearchInput(), query);
        component.waitForText(cssSelector("tbody > tr:nth-child(1)"), query);
    }

    @Step("Delete value in table")
    public void deleteDataInTable(){
        component.getDeleteButton().click();
        component.jsClick(component.getYesButton());
        component.assertThat(invisibilityOf(component.getYesButton()));
    }

    @Step("Checking the presence of a value in the table")
    public boolean isValueDisplayInTable(String value){
        sleep(1000);
        component.assertThat(visibilityOfElementLocated(tagName("td")));
        return component.isTextDisplayed(value, tagName("td"));
    }
}
