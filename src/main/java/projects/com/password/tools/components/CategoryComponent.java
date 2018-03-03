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
        return $(name("create-category-name"));
    }

    public WebElement getDescInput(){
        return $(name("create-category-description"));
    }

    public WebElement getOkCreateButton(){
        return $(cssSelector("#createDialogForm > div > div.card__actions > button:nth-child(1) > div"));
    }

    public WebElement getOkEditButton(){
        return $(cssSelector("#editDialogForm > div > div.card__actions > button:nth-child(1) > div"));
    }

    public WebElement getEditButton(){
        return $(cssSelector("td:nth-child(1) > button"));
    }

    public WebElement getEditNameInput(){
        return $(name("edit-category-name"));
    }

    public WebElement getEditDescInput(){
        return $(name("edit-category-description"));
    }

    public WebElement getDeleteButton(){
        return $(cssSelector("td:nth-child(3) > button"));
    }
}
