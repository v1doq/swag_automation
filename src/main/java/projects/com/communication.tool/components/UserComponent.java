package projects.com.communication.tool.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class UserComponent extends BaseComponent {

    public UserComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getCreateUserButton(){
        return $(cssSelector(".users__add-new > button"));
    }

    public WebElement getFirstNameInput(){
        return $(name("firstName"));
    }

    public WebElement getLastNameInput(){
        return $(name("lastName"));
    }

    public WebElement getEmailInput(){
        return $(name("email"));
    }

    public WebElement getPasswordInput(){
        return $(name("password"));
    }

    public WebElement getGeneratePassButton(){
        return $(className("icon is-small"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector(".modal-dialog__actions > button"));
    }

    public WebElement getEditButton(){
        return $(className("btn-edit"));
    }

    public WebElement getDeleteButton(){
        return $(className("btn-delete-icon"));
    }
}
