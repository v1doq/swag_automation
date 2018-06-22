package projects.com.swag.components.loginComponent;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import static org.openqa.selenium.By.*;

public class SignUpEmpComponents extends BaseComponent {

    public SignUpEmpComponents(WebDriver driver) {
        super(driver);
    }

    public WebElement getSignUpButton() {
        return $(linkText("/signup"));
    }

    public WebElement getFacebookButton() {
        return $(className("facebook"));
    }

    public WebElement getGoogleButton() {
        return $(className("google"));
    }

    public WebElement getLinkedInButton() {
        return $(className("linkedin"));
    }

    public WebElement getEmailButton() {
        return $(partialLinkText("Sign Up with Email"));
    }

    public WebElement getCreateEmployerButton() {
        return $(cssSelector("[href='/EmployerSignUp']"));
    }

    public WebElement getFirstNameInput() {
        return $(name("FirstName"));
    }

    public WebElement getLastNameInput() {
        return $(name("LastName"));
    }

    public WebElement getPhoneNumberInput() {
        return $(cssSelector("[placeholder='Number']"));
    }

    public WebElement getPhoneCodeMenu() {
        return $(className("has-value"));
    }

    public WebElement getExtension() {
        return $(cssSelector("[placeholder='Extension']"));
    }

    public WebElement getEmailInput() {
        return $(name("Email"));
    }

    public WebElement getCompanyNameInput() {
//        return $(className("Select-placeholder"));
        return $(id("react-select-3--value"));
    }

    public WebElement getTitleInput() {
        return $(name("Title"));
    }

    public WebElement getPasswordInput() {
        return $(name("Password"));
    }

    public WebElement getConfirmPasswordInput() {
        return $(name("ConfirmPassword"));
    }

    public WebElement getReferralCode() {
        return $(name("ReferralCode"));
    }

    public WebElement getAgreeCheckBox() {
        return $(xpath("//*[@id=\"geek-profile\"]/div/form/div/div[4]/div/div[1]/input"));
    }

    public WebElement getNextButton() {
        return $(cssSelector("div:nth-child(5) > div > button"));
    }

    public WebElement getFinishButton() {
        return $(className("button.emph"));
    }
}
