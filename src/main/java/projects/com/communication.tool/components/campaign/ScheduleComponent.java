package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class ScheduleComponent extends BaseComponent {

    public ScheduleComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getScheduleTab() {
        return $(cssSelector(".tabs>ul>li:nth-child(2)"));
    }

    public WebElement getIntervalInput() {
        return $(id("schedule-interval"));
    }

    public WebElement getStartTimeInput() {
        return $(name("startTime"));
    }

    public WebElement getEndTimeInput() {
        return $(name("endTime"));
    }

    public WebElement getSaveButton() {
        return $(cssSelector(".is-expanded>button"));
    }

    public By getDaysCheckbox() {
        return by(cssSelector("[name=weekDays] [role=checkbox]"));
    }
}
