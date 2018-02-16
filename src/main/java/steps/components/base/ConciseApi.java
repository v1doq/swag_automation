package steps.components.base;

import com.google.common.base.Function;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.List;
import java.util.concurrent.TimeUnit;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;
import static settings.SeleniumListener.LOG;

public abstract class ConciseApi {

    public abstract WebDriver getDriver();

    public void open(String url) {
        getDriver().get(url);
    }

    protected WebElement $(By locator) {
        return assertThat(visibilityOfElementLocated(locator));
    }

    protected void waitForText(By by, String value){
        LOG.info("Wait for text to be '" + value + "' " + by);
        new WebDriverWait(getDriver(), 15).until(ExpectedConditions.textToBe(by, value));
    }

    protected void waitForElementToBeClickable(WebElement element){
        LOG.info("Wait for element with type '" + element.getTagName() + "' to be clickable");
        new WebDriverWait(getDriver(), 15).until(ExpectedConditions.elementToBeClickable(element));
    }

    public void focusAndClick(WebElement element){
        Actions actions = new Actions(getDriver());
        LOG.info("Try to click on element with type: " + element.getTagName());
        actions.moveToElement(element).click().build().perform();
        LOG.info("Successfully clicked");
    }

    private <V> V assertThat(Function<? super WebDriver, V> condition) {
        return (new WebDriverWait(getDriver(), 15)).until(condition);
    }

    public boolean isElementPresent(By locator) {
        getDriver().manage().timeouts().implicitlyWait(0, TimeUnit.SECONDS);
        List<WebElement> list = getDriver().findElements(locator);
        getDriver().manage().timeouts().implicitlyWait(15, TimeUnit.SECONDS);
        return list.size() != 0 && list.get(0).isDisplayed();
    }

    public void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
