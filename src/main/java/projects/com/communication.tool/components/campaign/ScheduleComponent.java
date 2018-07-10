package projects.com.communication.tool.components.campaign;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import java.util.List;

import static org.openqa.selenium.By.*;

public class ScheduleComponent extends BaseComponent {

    public ScheduleComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getScheduleTab() {
        return $(linkText("Schedule"));
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
        return $(cssSelector(".schedule .btn-save"));
    }

    public List<WebElement> getDaysCheckbox() {
        return $$(cssSelector("[name=weekDays] [role=checkbox]"));
    }
}
