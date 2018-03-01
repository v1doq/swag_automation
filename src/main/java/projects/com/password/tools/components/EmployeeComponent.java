package projects.com.password.tools.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class EmployeeComponent extends BaseComponent {

    public EmployeeComponent(WebDriver driver) {
        super(driver);
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

    public WebElement getSubmitButton(){
        return $(cssSelector("button:nth-child(6)"));
    }

    //pop up
    public WebElement getNewPasswordInput(){
        return $(name("new password"));
    }

    public WebElement getOkButton(){
        return $(cssSelector("button:nth-child(1)"));
    }

    //edit
    public WebElement getUpdateButton(){
        return $(cssSelector("button:nth-child(5)"));
    }

    public WebElement getProfileTab(){
        return $(id("profile-tab-header"));
    }
}
