package common;

import com.google.common.base.Function;
import org.openqa.selenium.*;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.KeyEvent;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

import static java.awt.Toolkit.getDefaultToolkit;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static settings.SeleniumListener.LOG;

public abstract class ConciseApi {

    public abstract WebDriver getDriver();

    private static final long TIME_OUT = 15;

    protected WebElement $(By locator) {
        WebElement element = null;
        try {
            element = assertThat(visibilityOfElementLocated(locator));
        } catch (StaleElementReferenceException e) {
            LOG.info("Catch exception");
        }
        return element;
    }

    protected List<WebElement> $$(By locator) {
        return getDriver().findElements(locator);
    }

    protected By by(By locator) {
        assertThat(visibilityOfElementLocated(locator));
        return locator;
    }

    public <V> V assertThat(Function<? super WebDriver, V> condition) {
        return (new WebDriverWait(getDriver(), TIME_OUT)).until(condition);
    }

    public void clickToElement(WebElement element){
        assertThat(elementToBeClickable(element));
        element.click();
    }

    public void actionClick(WebElement element) {
        LOG.info("Move to element and click: " + element.getTagName());
        Actions actions = new Actions(getDriver());
        actions.moveToElement(element).click().build().perform();
    }

    public void actionSendKeys(WebElement element,String keys){
        LOG.info("Send keys to element: " + element);
        Actions actions = new Actions(getDriver());
        actions.sendKeys(element, keys).build().perform();
    }
    public void actionEnterPress(WebElement element){
        LOG.info("Enter pressed to element:" + element);
        Actions actions = new Actions(getDriver());
        actions.sendKeys(Keys.ENTER).build().perform();
    }

    public void jsClick(WebElement element) {
        LOG.info("Try to click on element with type: " + element.getTagName());
        ((JavascriptExecutor) getDriver()).executeScript("arguments[0].click()", element);
        LOG.info("Successfully clicked");
    }

    public WebElement getElementInListByText(String text, By locator) {
        LOG.info("Try to get element in list by text: " + text);
        List<WebElement> list = getDriver().findElements(locator);
        for (WebElement element : list) {
            if (element.getText().equals(text)) {
                return element;
            } else {
                LOG.info("Element with text: " + text + " not found");
            }
        }
        return null;
    }

    public void clearAndSendKeys(WebElement element, String text) {
        LOG.info("Try to clear field and sent keys: " + text);
        assertThat(elementToBeClickable(element));
        while (element.getAttribute("value").length() > 0) {
            element.sendKeys(Keys.BACK_SPACE);
        }
        element.sendKeys(text);
    }

    public boolean isElementPresent(By locator) {
        LOG.info("Is element present: " + locator);
        List<WebElement> list;
        try {
            implicitlyWait(0);
            list = getDriver().findElements(locator);
        } catch (StaleElementReferenceException e) {
            implicitlyWait(TIME_OUT);
            return false;
        }
        return list.size() != 0 && list.get(0).isDisplayed();
    }

    public boolean isTextDisplayed(String text, By locator) {
        LOG.info("Is text '" + text + "' displayed by " + locator);
        boolean isDisplayed = false;
        try {
            List<WebElement> list = getDriver().findElements(locator);
            for (WebElement element : list) {
                if (element.getText().contains(text)) {
                    isDisplayed = true;
                    break;
                }
            }
        } catch (StaleElementReferenceException e) {
            return false;
        }
        return isDisplayed;
    }

    public void waitForText(By by, String value) {
        LOG.info("Wait for part of text to be '" + value + "' " + by);
        assertThat(textMatches(by, Pattern.compile(value)));
    }

    public void waitForTextInList(By by, String value) {
        LOG.info("Wait for part of text to be '" + value + "' " + by);
        while (!isTextDisplayed(value, by)){
            sleep(500);
        }
    }

    public void open(String url) {
        getDriver().get(url);
    }

    public static void sleep(long time) {
        LOG.info("Sleep " + time + " milliseconds");
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    public void fullScreenMode() {
        LOG.info("Maximize window");
        getDriver().manage().window().maximize();
        sleep(500);
    }

    public void scrollUp(WebElement element) {
        LOG.info("Try to scroll up");
        ((JavascriptExecutor) getDriver()).executeScript("scroll(0, -250);");
        assertThat(elementToBeClickable(element));
    }

    private void implicitlyWait(long time) {
        getDriver().manage().timeouts().implicitlyWait(time, TimeUnit.SECONDS);
    }

    public static void uploadFileFromModalWindow(String filePath) {
        StringSelection ss = new StringSelection(filePath);
        getDefaultToolkit().getSystemClipboard().setContents(ss, null);
        sleep(1000);
        try {
            Robot robot = new Robot();
            LOG.info("Press VK_CONTROL");
            robot.keyPress(KeyEvent.VK_CONTROL);
            LOG.info("Press VK_V");
            robot.keyPress(KeyEvent.VK_V);
            LOG.info("Release VK_V");
            robot.keyRelease(KeyEvent.VK_V);
            LOG.info("Release VK_CONTROL");
            robot.keyRelease(KeyEvent.VK_CONTROL);
            LOG.info("Press VK_ENTER");
            robot.keyPress(KeyEvent.VK_ENTER);
            LOG.info("Release VK_ENTER");
            robot.keyRelease(KeyEvent.VK_ENTER);
        } catch (AWTException e) {
            e.printStackTrace();
        }
    }
}
