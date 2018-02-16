package steps.components.authorization;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import steps.components.base.BaseComponent;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;

public class RegisterComponent extends BaseComponent {

    public RegisterComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getFirstNameInput(){
        return $(xpath("//input"));
    }

    public WebElement getLastNameInput(){
        return $(xpath("//div[2]/div/input"));
    }

    public WebElement getEmailInput(){
        return $(xpath("//div[3]/div/input"));
    }

    public WebElement getPasswordInput(){
        return $(xpath("//div[4]/div/input"));
    }

    public WebElement getRepeatPasswordInput(){
        return $(xpath("//div[5]/div/input"));
    }

    public WebElement getSubmitButton(){
        return $(xpath("(//button[@type='button'])[2]"));
    }

    public void openRegistrationPopUp(){
        $(cssSelector("span:nth-child(5)>button")).click();
        waitForText(By.cssSelector("h2"), "Please, enter your registration information");
    }
}
