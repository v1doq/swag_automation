package projects.com.password.tools.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class ResourceComponent extends BaseComponent {

    public ResourceComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getUsernameInput(){
        return $(name("username"));
    }

    public WebElement getDescriptionInput(){
        return $(name("description"));
    }

    public WebElement getCategoryDropdown(){
        return $(name("category"));
    }

    public WebElement getTypeDropdown(){
        return $(name("type"));
    }

    public WebElement getPassInput(){
        return $(name("password"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("button:nth-child(7)"));
    }
}
