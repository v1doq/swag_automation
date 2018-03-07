package projects.com.password.tools.components;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class AdminComponent extends EmployeeComponent {

    public AdminComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getRepeatPasswordInput(){
        return $(name("repeat-password"));
    }

    public WebElement getNextButton(){
        return $(name("submit"));
    }

    //pop up
    public WebElement getYourPasswordInput(){
        return $(cssSelector("input[type=\"password\"]"));
    }

    public WebElement getCreateButton(){
        return $(cssSelector("button.btn.primary"));
    }
}
