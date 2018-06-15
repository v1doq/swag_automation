package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;

public class CampaignFiltersComponent extends BaseComponent {

    public CampaignFiltersComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getFiltersTab(){
        return $(cssSelector("div.tabs>ul>li:nth-child(3)"));
    }

    public WebElement getOpenPopUpButton(){
        return $(cssSelector(".campaign-preview__contacts-header button"));
    }

    public WebElement getAddToCampaignButton(){
        return $(cssSelector(".is-horizontal>button"));
    }

    public WebElement getEditFilterButton(){
        return $(cssSelector(".contacts-table__actions .btn-edit"));
    }

    public WebElement getDeleteFilterButton(){
        return $(cssSelector(".contacts-table__actions .btn-delete-icon"));
    }

    public By getAppliedFilterInTable(){
        return by(cssSelector(".contacts-table td"));
    }
}
