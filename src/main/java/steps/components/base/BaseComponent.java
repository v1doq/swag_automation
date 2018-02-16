package steps.components.base;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.cssSelector;

public class BaseComponent extends ConciseApi {

    private WebDriver driver;

    protected BaseComponent(WebDriver driver) {
        this.driver = driver;
    }

    @Override
    public WebDriver getDriver() {
        return driver;
    }

    public void waitForTextInH1(String text){
        waitForText(By.cssSelector("h1"), text);
    }

    public WebElement getH1(){
        return $(cssSelector("h1"));
    }
}
