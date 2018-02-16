package settings;

public enum Browser {
    FIREFOX("firefox"),
    CHROME("chrome"),
    CHROME_WIN("chrome_win"),
    IE("ie"),
    SAFARI("safari");
    private String browserName;

    Browser(String browserName) {
        this.browserName = browserName;
    }

    public String getBrowserName() {
        return browserName;
    }

    public static Browser getByName(String name){
        for(Browser browser : values()) {
            if(browser.getBrowserName().equalsIgnoreCase(name)) {
                return browser;
            }
        }
        return null;
    }
}
