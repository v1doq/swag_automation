package steps.components.problem;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import steps.components.base.BaseComponent;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.xpath;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class TestCaseComponent extends BaseComponent {

    public TestCaseComponent(WebDriver driver) {
        super(driver);
    }

    private WebElement getPlusButton(){
        return $(xpath("(//button[@type='button'])[14]"));
    }

    public void openTestCasePopUp(){
        assertThat(elementToBeClickable(getPlusButton()));
        getPlusButton().click();
    }

    public WebElement getInputPatternField(){
        return $(cssSelector("textarea"));
    }

    public WebElement getOutputPatternField(){
        return $(xpath("//div[2]/div/textarea"));
    }

    public WebElement getSubmitButton(){
        return $(xpath("(//button[@type='button'])[6]"));
    }
}
