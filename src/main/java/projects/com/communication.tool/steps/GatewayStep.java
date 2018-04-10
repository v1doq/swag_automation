package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.GatewayComponent;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class GatewayStep {

    private GatewayComponent component;
    public static final byte MIN_FROM_NAME_LENGTH = 1;
    private static final String GATEWAY_OUTLOOK_EMAIL = "communication.auto@outlook.com";
    private static final String GATEWAY_OUTLOOK_PASS = "Passcommunication1";
    public static final String GATEWAY_GMAIL_EMAIL = "communication.tool.test@gmail.com";
    private static final String GATEWAY_GMAIL_PASS = "passcommunication";

    public GatewayStep(WebDriver driver) {
        this.component = new GatewayComponent(driver);
    }

    @Step("Open gateways tab")
    public void openGatewayTab() {
        component.getGatewaysTab().click();
    }

    @Step("Create outlook gateway")
    public void createGateway(String fromName) {
        component.assertThat(elementToBeClickable(component.getFromNameInput()));
        component.getFromNameInput().sendKeys(fromName);
        component.getFromEmailInput().sendKeys(GATEWAY_OUTLOOK_EMAIL);
        component.getLoginInput().sendKeys(GATEWAY_OUTLOOK_EMAIL);
        component.getPasswordInput().sendKeys(GATEWAY_OUTLOOK_PASS);
        component.getSubmitButton().click();
        component.waitForText(component.getFromNameValue(), fromName);
        component.isElementPresent(component.getFromNameValue());
    }

    @Step("Create gmail gateway")
    public void createGmailGateway(String fromName) {
        component.assertThat(elementToBeClickable(component.getFromNameInput()));
        component.getFromNameInput().sendKeys(fromName);
        component.getFromEmailInput().sendKeys(GATEWAY_GMAIL_EMAIL);
        component.clearAndSendKeys(component.getSmtpHostInput(), "smtp.gmail.com:587");
        component.clearAndSendKeys(component.getImapHostInput(), "imap.gmail.com:993");
        component.getLoginInput().sendKeys(GATEWAY_GMAIL_EMAIL);
        component.getPasswordInput().sendKeys(GATEWAY_GMAIL_PASS);
        component.getSubmitButton().click();
        component.waitForText(component.getFromNameValue(), fromName);
        component.isElementPresent(component.getFromNameValue());
    }

    @Step("Verify that gateway was created")
    public boolean isFromNameDisplayedInGateway(String fromName) {
        return component.isTextDisplayed(fromName, component.getFromNameValue());
    }
}
