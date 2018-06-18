package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.FlowComponent;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static settings.SeleniumListener.LOG;

public class FlowStep {

    private FlowComponent component;
    public static final String WORK_EMAIL = "Work Email";
    private static final String PERSONAL_EMAIL = "Personal Email";
    private static final String ALTERNATE_EMAIL = "Alternate Email";

    public FlowStep(WebDriver driver) {
        this.component = new FlowComponent(driver);
    }

    @Step("Open flow tab")
    public void openFlowTab() {
        component.scrollUp();
        component.assertThat(elementToBeClickable(component.getFlowTab()));
        component.getFlowTab().click();
    }

    @Step("Create flow")
    public void createFlow(String type) {
        component.getCreateFlowButton().click();
        switch (type) {
            case WORK_EMAIL:
                component.getWorkEmailCard().click();
                break;
            case PERSONAL_EMAIL:
                component.getPersonalEmailCard().click();
                break;
            case ALTERNATE_EMAIL:
                component.getAlternateEmailCard().click();
                break;
            default:
                LOG.info("There is no flow with type: " + type);
        }
    }

    @Step("Verify that type is assigned to flow")
    public boolean isTypeAppliedToFlow(String type) {
        component.getFlowCard().click();
        component.waitForText(component.getFlowTypeSelect(), type);
        return component.isTextDisplayed(type, component.getFlowTypeSelect());
    }

    @Step("Save flow")
    public void saveFlow() {
        component.scrollUp();
        component.getSaveButton().click();
    }
}
