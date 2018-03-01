package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.ResourceComponent;
import projects.com.password.tools.components.TableComponent;

import static common.DefaultConstant.VALID_PASSWORD;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static settings.TestConfig.getProperty;

public class ResourceStep {

    private ResourceComponent component;
    private TableComponent tableComponent;
    private static final int MIN_USERNAME_LENGTH = 1;
    private static final int MIN_DESC_LENGTH = 1;

    public ResourceStep(WebDriver driver) {
        this.component = new ResourceComponent(driver);
        this.tableComponent = new TableComponent(driver);
    }

    @Step("Open resources page")
    public void openResourcePage() {
        component.open(getProperty("password.tools.url") + "dashboard/resources/");
    }

    @Step("Create new resource")
    public void createResource(String categoryName, String type) {
        tableComponent.getCreateButton().click();
        component.getUsernameInput().sendKeys(randomAlphabetic(MIN_USERNAME_LENGTH));
        component.getDescriptionInput().sendKeys(randomAlphabetic(MIN_DESC_LENGTH));
        component.getCategoryDropdown().sendKeys(categoryName);
        component.getTypeDropdown().sendKeys(type);
        component.getPassInput().sendKeys(VALID_PASSWORD);
        component.getSubmitButton().click();
    }
}
