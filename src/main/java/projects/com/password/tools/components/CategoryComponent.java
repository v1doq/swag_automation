package projects.com.password.tools.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class CategoryComponent extends BaseComponent {

    public CategoryComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getNameInput(){
        return $(name("create-name"));
    }

    public WebElement getDescInput(){
        return $(name("create-description"));
    }

    public WebElement getOkCreateButton(){
        return $(name("create-confirm"));
    }

    public WebElement getEditButton(){
        return $(cssSelector("td:nth-child(1) > button"));
    }

    public WebElement getEditNameInput(){
        return $(name("edit-name"));
    }

    public WebElement getEditDescInput(){
        return $(name("edit-description"));
    }

    public WebElement getOkEditButton(){
        return $(name("edit-confirm"));
    }

    public WebElement getDeleteButton(){
        return $(cssSelector("td:nth-child(3) > span > span > button"));
    }

    public WebElement getServerError(){
        return $(cssSelector(".delete-error-message"));
    }
}
