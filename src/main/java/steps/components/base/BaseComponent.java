package steps.components.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;

public class BaseComponent extends ConciseApi {

    private WebDriver driver;

    protected BaseComponent(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    public WebElement getH1(String text){
        waitForText(By.cssSelector("h1"), text);
        return $(cssSelector("h1"));
    }

    public WebElement getServerError(String text){
        assertThat(textToBe(cssSelector(".red--text"), text));
        return $(cssSelector(".red--text"));
    }
}
