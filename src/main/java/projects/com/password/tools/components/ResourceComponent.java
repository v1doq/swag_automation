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

    public WebElement getSharedTypeInDropdown(){
        return $(cssSelector(".menuable__content__active > div > ul > li:nth-child(2)"));
    }

    public WebElement getIndividualTypeInDropdown(){
        return $(cssSelector("li:nth-child(2)"));
    }

    public WebElement getPropertyDropdown(){
        return $(xpath(".//*[@id='app']/div[6]/section/div/div/div/div/div/div[2]/form/div[3]/div[2]/div[1]/div[1]/input"));
    }

    public WebElement getPassInput(){
        return $(name("password"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("button:nth-child(7)"));
    }

    public WebElement getShareButtonInTable(){
        return $(cssSelector("td:nth-child(7) > button"));
    }

    public WebElement getShareDropdown(){
        return $(cssSelector(".input-group--select__autocomplete"));
    }

    public WebElement getYourPassInput(){
        return $(cssSelector("input[type=\"password\"]"));
    }

    public WebElement getShareButton(){
        return $(cssSelector("button:nth-child(1)"));
    }
}
