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

    public WebElement getItemInDropdown(){
        return $(cssSelector(".menuable__content__active > div > ul"));
    }

    public WebElement getTypeDropdown(){
        return $(name("type"));
    }

    public WebElement getSharedTypeInDropdown(){
        return $(cssSelector(".menuable__content__active > div > ul > li:nth-child(2)"));
    }

    public WebElement getIndividualTypeInDropdown(){
        return $(cssSelector(".menuable__content__active > div > ul > li:nth-child(1)"));
    }

    public WebElement getPropertyDropdown(){
        return $(name("property"));
    }

    public WebElement getPassInput(){
        return $(name("password"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("button:nth-child(7)"));
    }

    public WebElement getShareButtonInTable(){
        return $(cssSelector("span:nth-child(2) > span > button"));
    }

    public WebElement getShareDropdown(){
        return $(name("share-to"));
    }

    public WebElement getYourPassInSharePopUpInput(){
        return $(name("share-password-password"));
    }

    public WebElement getShareButton(){
        return $(cssSelector("button:nth-child(1)"));
    }
}
