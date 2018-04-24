package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.RepresentativeComponent;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class RepresentativeStep {

    private RepresentativeComponent component;
    public static final byte MIN_FROM_NAME_LENGTH = 1;
    public static final String GATEWAY_OUTLOOK_EMAIL = "communication.auto@outlook.com";
    private static final String GATEWAY_OUTLOOK_PASS = "Passcommunication1";
    public static final String GATEWAY_GMAIL_EMAIL = "communication.tool.test@gmail.com";
    private static final String GATEWAY_GMAIL_PASS = "passcommunication";

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

    public void submitReps(String fromName){
        component.getRepsTitle().click();
        component.getSubmitButton().click();
        component.waitForText(component.getFromNameValue(), fromName);
        component.waitForText(component.getFromNameValue(), fromName);
        component.isElementPresent(component.getFromNameValue());
    }

    @Step("Create placeholder")
    private void createPlaceholder(String key, String value){
        component.getNewPlaceholderButton().click();
        component.getPlaceholderKeyInput().sendKeys(key, Keys.ENTER);
        component.getPlaceholderValueInput().sendKeys(value);
    }

    @Step("Create representative with placeholder")
    public void createRepsWithPlaceholder(String email, String fromName, String key, String value){
        createPlaceholder(key, value);
        createRepresentative(email, fromName);
        submitReps(fromName);
    }

    @Step("Verify that gateway was created")
    public boolean isFromNameDisplayedInRepsCard(String fromName) {
        return component.isTextDisplayed(fromName, component.getFromNameValue());
    }
}
