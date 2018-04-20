package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.GatewayComponent;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class RepresentativeStep {

    private GatewayComponent component;
    public static final byte MIN_FROM_NAME_LENGTH = 1;
    public static final String GATEWAY_OUTLOOK_EMAIL = "communication.tool@outlook.com";
    private static final String GATEWAY_OUTLOOK_PASS = "Passcommunication1";
    public static final String GATEWAY_GMAIL_EMAIL = "communication.tool.test@gmail.com";
    private static final String GATEWAY_GMAIL_PASS = "passcommunication";

    public RepresentativeStep(WebDriver driver) {
        this.component = new GatewayComponent(driver);
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
        component.getSubmitButton().click();
        component.waitForText(component.getFromNameValue(), fromName);
        component.isElementPresent(component.getFromNameValue());
    }

    @Step("Verify that gateway was created")
    public boolean isFromNameDisplayedInRepsCard(String fromName) {
        return component.isTextDisplayed(fromName, component.getFromNameValue());
    }
}
