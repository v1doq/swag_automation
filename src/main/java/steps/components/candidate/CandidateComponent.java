package steps.components.candidate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import steps.components.base.BaseComponent;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.By.xpath;

public class CandidateComponent extends BaseComponent {

    public CandidateComponent(WebDriver driver) {
        super(driver);
    }

    public void openCandidatePopUp(){
        $(cssSelector("div.card__title>button")).click();
    }

    public WebElement getFirstNameInput(){
        return $(xpath("(//input[@type='text'])[5]"));
    }

    public WebElement getLastNameInput(){
        return $(xpath("(//input[@type='text'])[6]"));
    }

    public WebElement getEmailInput(){
        return $(xpath("(//input[@type='text'])[7]"));
    }

    public WebElement getSubmitButton(){
        return $(xpath("(//button[@type='button'])[10]"));
    }

    public WebElement getSearchField(){
        return $(name("seacrh-candidate field"));
    }

    public void waitForSearchResult(String candidateEmail){
        waitForText(cssSelector("td:nth-child(2)"), candidateEmail);
    }
}
