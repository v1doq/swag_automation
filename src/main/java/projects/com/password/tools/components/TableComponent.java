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

    public WebElement getEditButton(){
        return $(cssSelector("td:nth-child(2) > a"));
    }

    public WebElement getChangePassButton(){
        return $(cssSelector("span:nth-child(1) > span > button"));
    }

    public WebElement getActivatePassButton(){
        return $(cssSelector("span:nth-child(2) > span > button"));
    }

    public WebElement getDeleteButton(){
        return $(cssSelector("span:nth-child(3) > span > button"));
    }

    public WebElement getYesButton(){
        return $(name("delete-confirm"));
    }
}
