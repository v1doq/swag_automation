package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class CampaignComponent extends BaseComponent {

    public CampaignComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getCreateCampaignButton(){
        return $(className("campaigns__add-new"));
    }

    public WebElement getCommunicationButton(){
        return $(className("v-input--selection-controls__ripple"));
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

    public By getServerError(){
        return by(className("error-message"));
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
    public By getCampaignNameInPreviewLocator(){
        return by(cssSelector(".campaign-preview__title .editable__text"));
    }

    public WebElement getCampaignNameInPreview(){
        return $(className("campaign-preview__title"));
    }

    public By getCampaignDescInPreviewLocator(){
        return by(cssSelector(".campaign-preview__description .editable__text"));
    }

    public WebElement getCampaignDescInPreview(){
        return $(className("campaign-preview__description"));
    }

    public WebElement getEditCampaignNameButton(){
        return $(cssSelector(".campaign-preview__title .edit-icon__content"));
    }

    public WebElement getEditCampaignDescButton(){
        return $(cssSelector(".campaign-preview__description .edit-icon__content"));
    }

    public WebElement getUpdateCampaignNameInput(){
        return $(cssSelector(".campaign-preview__title [name=input]"));
    }

    public WebElement getUpdateCampaignDescInput(){
        return $(cssSelector(".campaign-preview__description [name=textarea]"));
    }

    public WebElement getUpdateCampaignNameButton(){
        return $(cssSelector(".campaign-preview__title .btn-save"));
    }

    public WebElement getUpdateCampaignDescButton(){
        return $(cssSelector(".campaign-preview__description .btn-save"));
    }

    public WebElement getDeleteCampaignPreview(){
        return $(id("deleteCampaignButton"));
    }

    public WebElement getDeleteCampaignButton(){
        return $(className("menu-popup-link"));
    }

    public WebElement getConfirmDeletionButton(){
        return $(className("btn-delete"));
    }
}
