package projects.com.communication.tool.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import projects.com.communication.tool.components.ScheduleComponent;
import settings.SQLConnector;

import java.util.List;

import static common.ConciseApi.select;
import static settings.SeleniumListener.LOG;

public class ScheduleStep {

    private ScheduleComponent component;
    private static final String KIEV_TIME_ZONE = "(UTC+02:00) Helsinki, Kyiv, Riga, Sofia, Tallinn, Vilnius";

    public ScheduleStep(WebDriver driver) {
        this.component = new ScheduleComponent(driver);
    }

    @Step("Open schedule tab")
    public void openScheduleTab() {
        component.getScheduleTab().click();
    }

    @Step("Update all default schedule values")
    public void updateSchedule(String interval) {
        component.clearAndSendKeys(component.getIntervalInput(), interval);
        select(component.getTimeZoneSelect(), KIEV_TIME_ZONE);
        selectAllDays();
        component.getStartTimeInput().sendKeys("01:00");
        component.getEndTimeInput().sendKeys("23:00");
        component.getSaveButton().click();
    }

    private void selectAllDays() {
        List<WebElement> list = component.getDriver().findElements(component.getDaysCheckbox());
        for (WebElement element : list) {
            if (!element.isSelected()) {
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
        String interval = connector.getStringValueInDB("SELECT Schedule_Interval FROM CommunicationTool.dbo." +
                "EmailCommunication WHERE CampaignId = '" + campaignId + "'", "Schedule_Interval");
        connector.closeConnection();
        return interval;
    }
}