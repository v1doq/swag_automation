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
        return $(name("create-employee-name"));
    }

    public WebElement getUsernameInput(){
        return $(name("create-employee-username"));
    }

    public WebElement getTitleInput(){
        return $(name("create-employee-title"));
    }

    public WebElement getEmailInput(){
        return $(name("create-employee-email"));
    }

    public WebElement getPasswordInput(){
        return $(name("create-employee-password"));
    }

    public WebElement getSubmitButton(){
        return $(xpath(".//*[@id='app']/div[3]/section/div/div/div/div/div/div[2]/form/button[2]"));
    }

    //pop up
    public WebElement getYourPasswordInput(){
        return $(name("change-password-password"));
    }

    public WebElement getNewPasswordInput(){
        return $(name("change-password-password-new-password"));
    }

    public WebElement getOkButton(){
        return $(cssSelector("button:nth-child(1)"));
    }

    //edit
    public WebElement getEditUsernameInput(){
        return $(name("edit-employee-username"));
    }

    public WebElement getUpdateButton(){
        return $(xpath("//*[@id=\"profile-tab\"]/form/button[2]/div"));
    }

    public WebElement getProfileTab(){
        return $(id("profile-tab-header"));
    }
}
