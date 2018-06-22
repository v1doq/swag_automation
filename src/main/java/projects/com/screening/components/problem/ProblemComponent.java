package projects.com.screening.components.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import common.BaseComponent;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.By.xpath;

public class ProblemComponent extends BaseComponent {

    public ProblemComponent(WebDriver driver) {
        super(driver);
    }

    public void openProblemPopUp(){
        $(cssSelector("div.card__title>button")).click();
    }

    public WebElement getNameInput(){
        return $(xpath("(//input[@type='text'])[5]"));
    }

    public WebElement getDescriptionInput(){
        return $(xpath("//div[@id='app']/div[5]/div/div/form/div[2]/div/textarea"));
    }

    public WebElement getLanguageCheckbox(){
        return $(cssSelector(".input-group--selection-controls__ripple"));
    }

    public WebElement getSolutionTimeInput(){
        return $(xpath("(//input[@type='text'])[6]"));
    }

    public WebElement getExecutionTimeInput(){
        return $(xpath("(//input[@type='text'])[7]"));
    }

    public WebElement getExecutionMemoryInput(){
        return $(xpath("(//input[@type='text'])[8]"));
    }

    public WebElement getSubmitButton(){
        return $(xpath("(//button[@type='button'])[8]"));
    }

    public WebElement getSearchField(){
        return $(name("seacrh-problem field"));
    }

    public void waitForSearchResult(String problemName){
        waitForText(cssSelector("td:nth-child(1)"), problemName);
    }
}
