package projects.com.password.tools.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class EmployeeComponent extends BaseComponent {

    public EmployeeComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getCreateButton(){
        return $(cssSelector(".btn-route-icon"));
    }

    public WebElement getNameInput(){
        return $(name("name"));
    }

    public WebElement getUsernameInput(){
        return $(name("username"));
    }

    public WebElement getTitleInput(){
        return $(name("title"));
    }

    public WebElement getEmailInput(){
        return $(name("email"));
    }

    public WebElement getPasswordInput(){
        return $(name("password"));
    }

    public WebElement getCommentInput(){
        return $(cssSelector("div.input-group__input > textarea"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("button:nth-child(6)"));
    }
}
