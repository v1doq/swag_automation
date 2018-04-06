package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.GatewayComponent;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;

public class GatewayStep {

    private GatewayComponent component;
    public static final byte MIN_FROM_NAME_LENGTH = 1;
    private static final String EMAIL = "communication.tool@outlook.com";
    private static final String PASS = "Passcommunication1";

    public GatewayStep(WebDriver driver) {
        this.component = new GatewayComponent(driver);
    }

    @Step("Open gateways tab")
    public void openGatewayTab() {
        component.getGatewaysTab().click();
    }

    @Step("Create new gateway")
    public void createGateway(String fromName) {
        component.assertThat(elementToBeClickable(component.getFromNameInput()));
        component.getFromNameInput().sendKeys(fromName);
        component.getFromEmailInput().sendKeys(EMAIL);
        component.getLoginInput().sendKeys(EMAIL);
        component.getPasswordInput().sendKeys(PASS);
        component.getSubmitButton().click();
        component.waitForText(component.getFromNameValue(), fromName);
    }

    @Step("Verify that gateway was created")
    public boolean isFromNameDisplayedInGateway(String fromName) {
        return component.isTextDisplayed(fromName, component.getFromNameValue());
    }
}
