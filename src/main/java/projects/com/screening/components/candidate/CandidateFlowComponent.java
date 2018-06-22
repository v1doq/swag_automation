package projects.com.screening.components.candidate;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import common.BaseComponent;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;

public class CandidateFlowComponent extends BaseComponent {

    public CandidateFlowComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getEmailInput(){
        return $(xpath("//input"));
    }

    public WebElement getConfirmButton(){
        return $(cssSelector("button"));
    }

    public WebElement getStartButton(){
        return $(cssSelector("button.btn.primary"));
    }

    public WebElement getLanguageDropdown(){
        return $(className("input-group__selections"));
    }

    public WebElement getLanguageItem(){
        return $(cssSelector("div.list__tile__title"));
    }

    public WebElement getSolutionInput(){
        return $(cssSelector("textarea"));
    }

    public WebElement getRunButton(){
        return $(cssSelector("button.btn.secondary"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector("button.btn.primary"));
    }

    public void waitForCompilerResponse(String text){
        waitForText(By.cssSelector("div:nth-child(9)"), text);
    }
}
