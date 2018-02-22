package steps.components.candidate;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import steps.components.base.BaseComponent;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;

public class ScreeningComponent extends BaseComponent {

    public ScreeningComponent(WebDriver driver) {
        super(driver);
    }

    private WebElement getPlusButton(){
        return $(cssSelector("td:nth-child(4)>button"));
    }

    public WebElement getLanguageCheckbox(){
        return $(xpath("//div[@id='app']/div[4]/div/div/div[4]/div[3]/div/div/div"));
    }

    public WebElement getProblemCheckbox(){
        return $(xpath("//div[6]/div/div/div/div/div"));
    }

    public WebElement getSubmitButton(){
        return $(xpath("(//button[@type='button'])[6]"));
    }

    public WebElement getResultsButton(){
        return $(cssSelector("span.chip__content"));
    }

    public WebElement getAssignedUrlValue(){
        return $(cssSelector("strong"));
    }

    public void openScreeningPopUp(){
        assertThat(elementToBeClickable(getPlusButton()));
        getPlusButton().click();
    }
}
