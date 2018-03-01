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

    public WebElement getCreateButton(){
        return $(cssSelector(".btn-route-icon"));
    }

    public WebElement getSearchInput(){
        return $(name("Search"));
    }

    public WebElement getActionButton(){
        return $(cssSelector(".btn-row-action"));
    }

    public WebElement getEditButton(){
        return $(cssSelector("td:nth-child(2) > a"));
    }

    public WebElement getDeleteButton(){
        return $(cssSelector(".btn-row-action:nth-of-type(2)"));
    }

    public WebElement getYesButton(){
        return $(cssSelector("button:nth-child(2)"));
    }
}
