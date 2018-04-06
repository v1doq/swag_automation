package projects.com.communication.tool.components;

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

    public WebElement getFieldSelect(){
        return $(cssSelector(".is-grouped > div:nth-child(1) > div > select"));
    }

    public WebElement getCriterionSelect(){
        return $(cssSelector(".is-grouped > div:nth-child(2) > div > select"));
    }

    public WebElement getValueInput(){
        return $(cssSelector("div:nth-child(3) > section > div > div > input"));
    }

    public WebElement getCounterValue(){
        return $(className("filter-count"));
    }

    public By getCounter(){
        return by(className("filter-count"));
    }
}
