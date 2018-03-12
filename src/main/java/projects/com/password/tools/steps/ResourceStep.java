package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.ResourceComponent;
import projects.com.password.tools.components.TableComponent;
import settings.SQLConnector;

import static common.DefaultConstant.PASSWORD_PASS_TOOLS;
import static common.DefaultConstant.VALID_PASSWORD;
import static org.openqa.selenium.By.name;
import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class ResourceStep {

    private ResourceComponent component;
    private TableComponent tableComponent;
    public static final int MAX_RESOURCE_USERNAME_LENGTH = 50;
    public static final String RESOURCE_SHARED_TYPE = "Shared";

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
        component.assertThat(elementToBeClickable(tableComponent.getCreateButton()));
        tableComponent.getCreateButton().click();
        component.getUsernameInput().sendKeys(username);
        component.getDescriptionInput().sendKeys(username);
        component.getCategoryDropdown().sendKeys(categoryName);
        component.getItemInDropdown().click();
        component.getTypeDropdown().click();
        if (type.equals(RESOURCE_SHARED_TYPE)){
            component.getSharedTypeInDropdown().click();
        } else {
            component.getIndividualTypeInDropdown().click();
        }
        component.getPropertyDropdown().sendKeys(property, Keys.ENTER);
        component.getPassInput().sendKeys(VALID_PASSWORD);
        component.getSubmitButton().click();
    }

    @Step("Share a resource with user")
    public void shareResourceWithUser(String username) {
        component.fullScreenMode();
        component.getShareButtonInTable().click();
        component.getShareDropdown().sendKeys(username, Keys.ENTER);
        component.getYourPassInSharePopUpInput().sendKeys(PASSWORD_PASS_TOOLS);
        component.getShareButton().click();
        component.assertThat(invisibilityOfElementLocated(name("share-password-password")));
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
