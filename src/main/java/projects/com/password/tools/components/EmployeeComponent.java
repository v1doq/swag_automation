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
        return $(name("submit"));
    }

    //pop up
    public WebElement getYourPasswordInput(){
        return $(name("change-password-password"));
    }

    public WebElement getNewPasswordInput(){
        return $(name("change-password-password-new-password"));
    }

    public WebElement getChangePassSubmitButton(){
        return $(name("change-password-submit"));
    }

    //edit
    public WebElement getEditUsernameInput(){
        return $(name("username"));
    }

    public WebElement getUpdateButton(){
        return $(name("submit"));
    }

    public WebElement getProfileTab(){
        return $(id("profile-tab-header"));
    }
}
