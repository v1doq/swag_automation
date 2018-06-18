package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;

public class FlowComponent extends BaseComponent {

    public FlowComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getFlowTab() {
        return $(cssSelector(".tabs>ul>li:nth-child(4)"));
    }

    public WebElement getCreateFlowButton(){
        return $(cssSelector(".flow-stages [type=button]"));
    }

    public WebElement getWorkEmailCard(){
        return $(cssSelector(".flow-card--work-email"));
    }

    public WebElement getPersonalEmailCard(){
        return $(cssSelector(".flow-card--personal-email"));
    }

    public WebElement getAlternateEmailCard(){
        return $(cssSelector(".flow-card--alternate-email"));
    }

    public WebElement getEditFlowNameButton(){
        return $(cssSelector(".flow-stage--template button.btn-edit"));
    }

    public WebElement getFlowNameInput(){
        return $(name("input"));
    }

    public WebElement getFlowCard(){
        return $(cssSelector(".flow-card"));
    }

    public By getFlowTypeSelect(){
        return by(cssSelector(".flow-stage--template .input-group__selections__comma"));
    }

    public WebElement getSaveButton(){
        return $(cssSelector(".flow-stages .btn-save"));
    }
}
