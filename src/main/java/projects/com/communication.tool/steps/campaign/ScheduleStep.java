package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import projects.com.communication.tool.components.campaign.ScheduleComponent;
import settings.SQLConnector;

import java.util.List;

import static projects.com.communication.tool.common.CommStackDB.*;
import static settings.SQLConnector.*;

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
        SQLConnector con = new SQLConnector();
        String campaignId = con.getValueInDb(SELECT_FROM + CAMPAIGN_DB + WHERE + "Name = '" + campaignName + "'", "Id");
        return con.getValueInDb(SELECT_FROM + CAMPAIGN_DB + WHERE + "Id = '" + campaignId + "'", "Schedule_Interval");
    }
}
