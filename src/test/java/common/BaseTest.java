package common;

import io.qameta.allure.Attachment;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.remote.ScreenshotException;
import org.openqa.selenium.safari.SafariDriver;
import org.openqa.selenium.support.events.EventFiringWebDriver;
import org.testng.ITestResult;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import settings.Browser;
import settings.SeleniumListener;
import settings.TestConfig;

import java.util.concurrent.TimeUnit;

import static java.lang.System.getProperty;

public class BaseTest {

    protected WebDriver driver;

    @BeforeMethod(description = "Open browser", alwaysRun = true)
    public void openBrowser() {
        if (driver == null) {
            Browser browser = TestConfig.getBrowser();
            switch (browser) {
                case FIREFOX:
                    System.setProperty("webdriver.gecko.driver", getProperty("user.dir") + "/drivers/geckodriver");
                    EventFiringWebDriver eventDriver = new EventFiringWebDriver(new FirefoxDriver());
                    eventDriver.register(new SeleniumListener());
                    driver = eventDriver;
                    break;
                case CHROME:
                    System.setProperty("webdriver.chrome.driver", getProperty("user.dir") + "/drivers/chromedriver");
                    eventDriver = new EventFiringWebDriver(new ChromeDriver());
                    eventDriver.register(new SeleniumListener());
                    driver = eventDriver;
                    break;
                case CHROME_WIN:
                    System.setProperty("webdriver.chrome.driver", getProperty("user.dir") + "/drivers/chromedriver.exe");
                    eventDriver = new EventFiringWebDriver(new ChromeDriver());
                    eventDriver.register(new SeleniumListener());
                    driver = eventDriver;
                    break;
                case IE:
                    driver = new InternetExplorerDriver();
                    break;
                case SAFARI:
                    driver = new SafariDriver();
                    break;
                default:
                    throw new IllegalStateException("Unsupported browser type");
            }
        }
        driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);
    }

    @AfterMethod(description = "Close browser", alwaysRun = true)
    public void closeBrowser(ITestResult result) {
        if (!result.isSuccess()) {
            try {
                captureScreenshot();
            } catch (ScreenshotException e) {
                e.printStackTrace();
            }
        }
        if (driver != null) {
            driver.quit();
            driver = null;
        }
    }

    void addCookies(){
        Cookie cookie = new Cookie("rememberMe", "false");
        driver.manage().addCookie(cookie);
    }

    @Attachment(value = "Screenshot", type = "image/png")
    private byte[] captureScreenshot() {
        return ((TakesScreenshot) driver).getScreenshotAs(OutputType.BYTES);
    }
}
