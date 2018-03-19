package common;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static settings.SeleniumListener.LOG;

public abstract class ConciseApi {

    public abstract WebDriver getDriver();

    public void open(String url) {
        getDriver().get(url);
    }

    protected WebElement $(By locator) {
        WebElement element = null;
        try {
            element = assertThat(visibilityOfElementLocated(locator));
        } catch (StaleElementReferenceException e){
            LOG.info("Catch exception");
        }
        return element;
    }

    public <V> V assertThat(Function<? super WebDriver, V> condition) {
        return (new WebDriverWait(getDriver(), 15)).until(condition);
    }

    public void waitForText(By by, String value){
        LOG.info("Wait for text to be '" + value + "' " + by);
        assertThat(textToBe(by, value));
    }

    public void waitForPartOfText(By by, String value){
        LOG.info("Wait for part of text to be '" + value + "' " + by);
        assertThat(textMatches(by, Pattern.compile(value)));
    }

    public boolean isElementPresent(By locator) {
        getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        List<WebElement> list = getDriver().findElements(locator);
        getDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        return list.size() != 0 && list.get(0).isDisplayed();
    }

    public void jsClick(WebElement element){
        LOG.info("Try to click on element with type: " + element.getTagName());
        ((JavascriptExecutor)getDriver()).executeScript("arguments[0].click()", element);
        LOG.info("Successfully clicked");
    }

    public void clearAndSendKeys(WebElement element, String text){
        assertThat(elementToBeClickable(element));
        assertThat(visibilityOf(element));
        while(element.getAttribute("value").length() > 0) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        element.sendKeys(text);
    }

    public static void select(WebElement element, String text) {
        Select dropdown = new Select(element);
        dropdown.selectByVisibleText(text);
    }

    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void actionClick(WebElement element){
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element).click().build().perform();
    }

    public boolean isTextDisplayed(String text, String locator) {
        sleep(1000);
        boolean isDisplayed = false;
        List<WebElement> list = getDriver().findElements(tagName((locator)));
        for (WebElement element : list) {
            if (element.getText().equals(text)) {
                isDisplayed = true;
                break;
            }
        }
        return isDisplayed;
    }

    public void fullScreenMode(){
        getDriver().manage().window().maximize();
        sleep(500);
    }

    protected void refreshPage(){
        getDriver().navigate().refresh();
    }
}
