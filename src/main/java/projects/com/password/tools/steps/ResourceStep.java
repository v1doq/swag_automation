package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.ResourceComponent;
import projects.com.password.tools.components.TableComponent;
import settings.SQLConnector;

import static common.DefaultConstant.PASSWORD_PASS_TOOLS;
import static common.DefaultConstant.VALID_PASSWORD;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class ResourceStep {

    private ResourceComponent component;
    private TableComponent tableComponent;
    private static final int MIN_DESC_LENGTH = 1;
    public static final int MAX_RESOURCE_USERNAME_LENGTH = 50;

    public ResourceStep(WebDriver driver) {
        this.component = new ResourceComponent(driver);
        this.tableComponent = new TableComponent(driver);
    }

    @Step("Open resources page")
    public void openResourcePage() {
        component.open(getProperty("password.tools.url") + "dashboard/resources/");
    }

    @Step("Create new resource")
    public void createResource(String username, String categoryName, String type, String property) {
        tableComponent.getCreateButton().click();
        component.getUsernameInput().sendKeys(username);
        component.getDescriptionInput().sendKeys(randomAlphabetic(MIN_DESC_LENGTH));
        component.getCategoryDropdown().sendKeys(categoryName, Keys.ENTER);
        component.getTypeDropdown().click();
        if (type.equals("Shared")){
            component.getSharedTypeInDropdown().click();
        } else {
            component.getIndividualTypeInDropdown().click();
        }
        component.getPropertyDropdown().sendKeys(property);
        component.getPassInput().sendKeys(VALID_PASSWORD);
        component.getSubmitButton().click();
    }

    @Step("Share a resource with user")
    public void shareResourceWithUser(String username) {
        component.fullScreenMode();
        component.getShareButtonInTable().click();
        component.getShareDropdown().sendKeys(username, Keys.ENTER);
        component.getYourPassInput().sendKeys(PASSWORD_PASS_TOOLS);
        component.getShareButton().click();
        component.assertThat(invisibilityOfElementLocated(cssSelector("input[type=\"password\"]")));
    }

    @Step("Delete the resource in the database")
    public void deleteResourceInDB(String resourceName) {
        LOG.info("Try to delete resource in the database with name: " + resourceName);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM PasswordsTool.dbo.ResourceCategories WHERE Name='" + resourceName + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }
}
