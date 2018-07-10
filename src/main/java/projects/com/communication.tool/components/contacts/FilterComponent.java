package projects.com.communication.tool.components.contacts;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;

public class FilterComponent extends BaseComponent {

    public FilterComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getEntityInput(){
        return $(cssSelector("[placeholder = \"Select Entity\"]"));
    }

    public WebElement getFieldInput(){
        return $(cssSelector("[placeholder = \"Select Field\"]"));
    }

    public WebElement getCriterionInput(){
        return $(cssSelector("[placeholder = \"Select Criterion\"]"));
    }

    public WebElement getValueInput(){
        return $(cssSelector("[placeholder = \"Specify Value\"]"));
    }

    public By getSearchResult(){
        return by(className("v-list__tile__mask"));
    }

    public WebElement getCountOfAllFilters(){
        return $(className("filter-count-preview"));
    }

    public By getCountOfOneFilter(){
        return by(className("filter-count"));
    }
}
