package settings;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;

import static settings.SeleniumListener.LOG;

public class LocalStorage {
    private JavascriptExecutor js;

    public LocalStorage(WebDriver webDriver) {
        this.js = (JavascriptExecutor) webDriver;
    }

    public void removeItemFromLocalStorage(String item) {
        js.executeScript(String.format("window.localStorage.removeItem('%s');", item));
    }

    public boolean isItemPresentInLocalStorage(String item) {
        return !(js.executeScript(String.format("return window.localStorage.getItem('%s');", item)) == null);
    }

    public String getValueFromLocalStorage(String key) {
        LOG.info("Get value by: '" + key + "' key from local storage");
        return (String) js.executeScript(String.format("return window.localStorage.getItem('%s');", key));
    }

    public String getKeyFromLocalStorage(int key) {
        return (String) js.executeScript(String.format("return window.localStorage.key('%s');", key));
    }

    public Long getLocalStorageLength() {
        return (Long) js.executeScript("return window.localStorage.length;");
    }

    public void setItemInLocalStorage(String key, String value) {
        LOG.info("Set to local storage key: '" + key + "' and value: '" + value + "'");
        js.executeScript(String.format("window.localStorage.setItem('%s','%s');", key, value));
    }

    public void clearLocalStorage() {
        js.executeScript("window.localStorage.clear();");
    }
}
