package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.GatewayComponent;
import settings.SQLConnector;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static settings.SeleniumListener.LOG;

public class GatewayStep {

    private GatewayComponent component;
    public static final byte MIN_FROM_NAME_LENGTH = 1;
    private static final String EMAIL = "communication.tool.test@gmail.com";
    private static final String HOST = "smtp.gmail.com";
    private static final String PORT = "587";
    private static final String PASS = "passcommunication";

    public GatewayStep(WebDriver driver) {
        this.component = new GatewayComponent(driver);
    }

    @Step("Open gateways page")
    public void openGatewayTab() {
        component.getGatewaysTab().click();
    }

    @Step("Create new gateway")
    public void createGateway(String fromName) {
        component.assertThat(elementToBeClickable(component.getFromNameInput()));
        component.getFromNameInput().sendKeys(fromName);
        component.getFromEmailInput().sendKeys(EMAIL);
        component.getHostInput().sendKeys(HOST);
        component.getPortInput().sendKeys(PORT);
        component.getLoginInput().sendKeys(EMAIL);
        component.getPasswordInput().sendKeys(PASS);
        component.getSubmitButton().click();
        component.waitForText(component.getFromNameValue(), fromName);
    }

    @Step("Verify that gateway was created")
    public boolean isFromNameDisplayedInGateway(String fromName) {
        return component.isTextDisplayed(fromName, component.getFromNameValue());
    }

    @Step("Delete company in the database")
    public void deleteGatewayInDB(String fromName) {
        LOG.info("Delete gateway in the database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM CommunicationTool.dbo.EmailGateway WHERE Name = '" + fromName + "'");
        LOG.info("Successfully deleted");
        connector.closeConnection();
    }
}
