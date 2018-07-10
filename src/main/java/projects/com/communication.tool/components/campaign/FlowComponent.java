package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.id;
import static org.openqa.selenium.By.linkText;

public class FlowComponent extends BaseComponent {

    public FlowComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getFlowTab() {
        return $(linkText("Flow"));
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

    public WebElement getFlowTitleInput(){
        return $(id("flow-title"));
    }

    public WebElement getFlowCard(){
        return $(cssSelector(".flow-card"));
    }

    public By getFlowTypeSelect(){
        return by(cssSelector(".flow-template-dialogue .v-select__selections"));
    }

    public WebElement getSaveButton(){
        return $(cssSelector(".flow-template-dialogue__header .btn-save"));
    }
}
