package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.RepresentativeComponent;

import static common.ConciseApi.sleep;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class RepresentativeStep {

    private RepresentativeComponent component;
    public static final byte MIN_FROM_NAME_LENGTH = 1;
    public static final String GATEWAY_OUTLOOK_EMAIL = "communication.auto@outlook.com";
    public static final String GATEWAY_OUTLOOK_PASS = "Passcommunication1";
    public static final String GATEWAY_GMAIL_EMAIL = "communication.tool.test@gmail.com";
    public static final String GATEWAY_GMAIL_PASS = "passcommunication";

    public RepresentativeStep(WebDriver driver) {
        this.component = new RepresentativeComponent(driver);
    }

    @Step("Create representative")
    public void createRepresentative(String email, String fromName) {
        component.assertThat(elementToBeClickable(component.getFromNameInput()));
        component.getFromNameInput().sendKeys(fromName);
        if (email.equals(GATEWAY_OUTLOOK_EMAIL)) {
            component.getFromEmailInput().sendKeys(email);
            component.getLoginInput().sendKeys(email);
            component.getPasswordInput().sendKeys(GATEWAY_OUTLOOK_PASS);
        } else {
            component.getFromEmailInput().sendKeys(email);
            component.getLoginInput().sendKeys(email);
            component.getPasswordInput().sendKeys(GATEWAY_GMAIL_PASS);
            component.jsClearAndSendKeys(component.getSmtpHostInput(), "smtp.gmail.com:587");
            component.jsClearAndSendKeys(component.getImapHostInput(), "imap.gmail.com:993");
        }
    }

    @Step("Save representative")
    public void saveRepresentative() {
        component.getRepsTitle().click();
        component.getSubmitButton().click();
    }

    @Step("Is representative was created")
    public boolean isRepresentativeCreated() {
        boolean isCreated = false;
        while (component.isElementPresent(name("login"))) {
            sleep(1000);
            if (component.isTextDisplayed("settings error", className("error-message"))) {
                isCreated = false;
                break;
            }
        }
        if (!component.isElementPresent(name("login"))) {
            isCreated = true;
        }
        return isCreated;
    }

    @Step("Create representative with placeholder")
    public void createRepsWithPlaceholder(String email, String fromName, String key, String value) {
        createPlaceholder(key, value);
        createRepresentative(email, fromName);
        saveRepresentative();
    }

    @Step("Fill representative's fields")
    public void fillRepsFields(String email, String fromName) {
        if (component.isElementPresent(cssSelector(".gateways__add-new > button"))) {
            component.jsClick(component.getAddButton());
        }
        component.assertThat(elementToBeClickable(component.getFromNameInput()));
        component.getFromNameInput().sendKeys(fromName);
        component.getFromEmailInput().sendKeys(email);
    }

    @Step("Fill gateway's fields")
    public void fillGatewayFields(String email, String pass, String smtp, String imap) {
        component.getLoginInput().sendKeys(email);
        component.getPasswordInput().sendKeys(pass);
        component.jsClearAndSendKeys(component.getSmtpHostInput(), smtp);
        component.jsClearAndSendKeys(component.getImapHostInput(), imap);
    }

    @Step("Create placeholder")
    private void createPlaceholder(String key, String value) {
        component.getNewPlaceholderButton().click();
        component.getPlaceholderKeyInput().sendKeys(key, Keys.ENTER);
        component.getPlaceholderValueInput().sendKeys(value);
    }
}
