package projects.com.screening.components.authorization;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import common.BaseComponent;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class RegisterComponent extends BaseComponent {

    public RegisterComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getFirstNameInput(){
        return $(xpath("//input[@type='text']"));
    }

    public WebElement getLastNameInput(){
        return $(xpath("(//input[@type='text'])[2]"));
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
        assertThat(elementToBeClickable(xpath("(//button[@type='button'])[2]")));
    }

    public boolean isFirstNameErrorDisplayed(){
        return isElementPresent(xpath(".//*[@id='app']/div[2]/div/div/div[3]/div[1]/div[2]/div[1]/div"));
    }

    public boolean isLastNameErrorDisplayed(){
        return isElementPresent(xpath(".//*[@id='app']/div[2]/div/div/div[3]/div[2]/div[2]/div[1]/div"));
    }

    public boolean isEmailErrorDisplayed(){
        return isElementPresent(xpath("//div[3]/div[3]/div[2]/div/div"));
    }

    public boolean isPasswordErrorDisplayed(){
        return isElementPresent(xpath("//div[4]/div[2]/div/div"));
    }

    public boolean isRepeatPasswordErrorDisplayed(){
        return isElementPresent(xpath("//div[5]/div[2]/div/div"));
    }
}
