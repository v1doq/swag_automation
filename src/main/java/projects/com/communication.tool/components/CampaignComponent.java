package projects.com.communication.tool.components;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class CampaignComponent extends BaseComponent {

    public CampaignComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getCreateCampaignButton(){
        return $(cssSelector("div.campaigns__filter-header > div > button"));
    }

    public WebElement getNameInput(){
        return $(cssSelector("div.modal-body > div.control.is-horizontal > div > div:nth-child(1) > input"));
    }

    public WebElement getCompanySelect(){
        return $(cssSelector("div.multiselect__tags > span > span"));
    }

    public WebElement getCompanyInput(){
        return $(name("client"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("div.modal-dialog__actions > button"));
    }
}
