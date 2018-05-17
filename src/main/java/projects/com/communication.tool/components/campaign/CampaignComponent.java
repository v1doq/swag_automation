package projects.com.communication.tool.components.campaign;

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
        return $(className("campaigns__add-new"));
    }

    public WebElement getStartCommunicationButton(){
        return $(className("input-group--selection-controls__ripple"));
    }

    public By getSendingStatus(){
        return by(className("campaign-preview__progress-status"));
    }

    //POP_UP
    public WebElement getCampaignNameInput(){
        return $(name("name"));
    }

    public WebElement getCompanyNameInput(){
        return $(name("company"));
    }

    public WebElement getModalTitle(){
        return $(className("modal-title"));
    }


    public WebElement getSubmitButton(){
        return $(cssSelector(".modal-dialog__actions > button"));
    }

    public By getServerErrorInPopUp(){
        return by(cssSelector(".modal-header > p"));
    }

    public By getServerErrorInPreview(){
        return by(cssSelector("p.error-message"));
    }

    public By getCampaignNameError(){
        return by(cssSelector(".input-group__details > div"));
    }

    public By getCompanyNameError(){
        return by(cssSelector("div:nth-child(2) > div > div.input-group__details > div"));
    }

    //CAMPAIGN_LIST
    public WebElement getSearchInput(){
        return $(name("search"));
    }

    public By getCampaignInList(){
        return by(className("campaign-item"));
    }

    public By getCompanyInList(){
        return by(className("card-header-title"));
    }

    //PREVIEW
    public By getCampaignNameInPreview(){
        return by(className("campaign-preview__title"));
    }

    public By getCampaignDescInPreview(){
        return by(className("campaign-preview__description"));
    }

    public WebElement getEditCampaignNameButton(){
        return $(className("editable__text"));
    }

    public WebElement getUpdateCampaignNameInput(){
        return $(cssSelector(".editable__text-field > div > div > div.input-group__input > input[type=\"text\"]"));
    }

    public WebElement getUpdateCampaignNameButton(){
        return $(className("btn-save"));
    }

    public WebElement getEditCampaignDescButton(){
        return $(className("campaign-preview__description"));
    }

    public WebElement getUpdateCampaignDescInput(){
        return $(cssSelector(".editable__text-field > div > div > div.input-group__input > textarea"));
    }

    public WebElement getUpdateCampaignDescButton(){
        return $(cssSelector("p.campaign-preview__description > div > div.editable__block.editable__block--edit > " +
                "div.editable__actions > button.btn-save"));
    }
}
