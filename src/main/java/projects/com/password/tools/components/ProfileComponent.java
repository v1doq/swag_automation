package projects.com.password.tools.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class ProfileComponent extends BaseComponent {

    public ProfileComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getEditProfileButton(){
        return $(cssSelector("button.user-name"));
    }

    public WebElement getNameInput(){
        return $(name("edit-profile-name"));
    }

    public WebElement getUsernameInput(){
        return $(name("edit-profile-username"));
    }

    public WebElement getTitleInput(){
        return $(name("edit-profile-title"));
    }

    public WebElement getEmailInput(){
        return $(name("edit-profile-email"));
    }

    public WebElement getChangePassButton(){
        return $(name("edite-profile-change-password"));
    }

    public WebElement getYourPassInput(){
        return $(name("change-password-password"));
    }

    public WebElement getNewPassInput(){
        return $(name("change-password-password-new-password"));
    }

    public WebElement getOkButton(){
        return $(name("change-password-submit"));
    }

    public WebElement getSubmitButton(){
        return $(name("edit-profile-submit"));
    }
}
