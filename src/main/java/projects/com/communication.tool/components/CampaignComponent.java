package projects.com.communication.tool.components;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class CampaignComponent extends BaseComponent {

    public CampaignComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getCreateCampaignButton(){
        return $(cssSelector("div.campaigns__filter-header > div > button"));
    }

    public WebElement getNameInput(){
        return $(name("name"));
    }

    public WebElement getCompanyInput(){
        return $(name("company"));
    }

    public WebElement getModalTitle(){
        return $(className("modal-title"));
    }

    public WebElement getSearchInput(){
        return $(name("search"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector(".modal-dialog__actions > button"));
    }


    public By getCampaignNameInPreview(){
        return by(cssSelector("button.editable__text"));
    }

    public By getCampaignInList(){
        return by(className("campaign-item"));
    }

    public By getCompanyInList(){
        return by(className("card-header-title"));
    }

    public WebElement getStartCommunicationButton(){
        return $(cssSelector("div.input-group--selection-controls__ripple"));
    }

    public By getCampaignNameError(){
        return by(cssSelector(".input-group__details > div"));
    }

    public By getCompanyNameError(){
        return by(cssSelector("div:nth-child(2) > div > div.input-group__details > div"));
    }

    public By getSendingStatus(){
        return by(className("campaign-preview__progress-status"));
    }
}
