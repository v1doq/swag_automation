package projects.com.communication.tool.components.contacts;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;

public class FiltersComponent extends BaseComponent {

    public FiltersComponent(WebDriver driver) {
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
        return by(className("list__tile__mask"));
    }

    public WebElement getCounterValue(){
        return $(className("filter-count"));
    }

    public By getCounter(){
        return by(className("filter-count"));
    }
}
