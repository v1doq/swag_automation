package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import projects.com.communication.tool.components.campaign.ScheduleComponent;
import settings.SQLConnector;

import java.util.List;

import static settings.SeleniumListener.LOG;

public class ScheduleStep {

    private ScheduleComponent component;

    public ScheduleStep(WebDriver driver) {
        this.component = new ScheduleComponent(driver);
    }

    @Step("Open schedule tab")
    public void openScheduleTab() {
        component.scrollUp(component.getScheduleTab());
        component.getScheduleTab().click();
    }

    @Step("Update all default schedule values")
    public void updateSchedule(String interval) {
        component.clearAndSendKeys(component.getIntervalInput(), interval);
        selectAllDays();
        component.getStartTimeInput().sendKeys("01:00 AM");
        component.getEndTimeInput().sendKeys("11:00 PM");
        component.getSaveButton().click();
    }

    private void selectAllDays() {
        List<WebElement> list = component.getDriver().findElements(component.getDaysCheckbox());
        for (WebElement element : list) {
            if (element.getAttribute("aria-checked").contains("false")) {
                element.click();
            }
        }
    }

    @Step("Verify that schedule is successfully updated")
    public String getScheduleIntervalInDB(String campaignName) {
        LOG.info("Get campaign id in the database");
        SQLConnector connector = new SQLConnector();
        String campaignId = connector.getStringValueInDB("SELECT Id FROM CommunicationTool.dbo." +
                "Campaign WHERE Name = '" + campaignName + "'", "Id");
        LOG.info("Get schedule interval in the database");
        return connector.getStringValueInDB("SELECT Schedule_Interval FROM CommunicationTool.dbo." +
                "Campaign WHERE Id = '" + campaignId + "'", "Schedule_Interval");
    }
}
