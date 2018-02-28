package projects.com.password.tools.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class TableComponent extends BaseComponent {

    public TableComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getSearchInput(){
        return $(name("Search"));
    }

    public WebElement getChangePassButton(){
        return $(cssSelector(".btn-row-action"));
    }

    public WebElement getDeleteButton(){
        return $(cssSelector(".btn-row-action:nth-of-type(2)"));
    }
}
