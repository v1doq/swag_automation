package steps.components.base;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.textToBe;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
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
        } catch (Exception e){
            LOG.info("Catch exception");
        }
        return element;
    }

    public <V> V assertThat(Function<? super WebDriver, V> condition) {
        return (new WebDriverWait(getDriver(), 15)).until(condition);
    }

    protected void waitForText(By by, String value){
        LOG.info("Wait for text to be '" + value + "' " + by);
        assertThat(textToBe(by, value));
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

    public void clearAndType(WebElement element, String text) {
        element.sendKeys(Keys.CONTROL, "a");
        element.sendKeys(Keys.DELETE);
        element.sendKeys(text);
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
}
