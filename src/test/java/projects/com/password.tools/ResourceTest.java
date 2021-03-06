package projects.com.password.tools;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.password.tools.steps.*;

import static common.DefaultConstant.VALID_PASSWORD;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertFalse;
import static org.testng.Assert.assertTrue;
import static projects.com.password.tools.steps.CategoryStep.MAX_CATEGORY_LENGTH;
import static projects.com.password.tools.steps.EmployeeStep.MAX_USER_NAME_LENGTH;
import static projects.com.password.tools.steps.PropertyStep.MAX_PROPERTY_LENGTH;
import static projects.com.password.tools.steps.ResourceStep.*;

@Feature("Resources")
@Story("Functional tests for CRUD and share resource")
public class ResourceTest extends BaseTest {

    private TableStep tableStep;
    private ResourceStep resourceStep;
    private CategoryStep categoryStep;
    private EmployeeStep employeeStep;
    private PropertyStep propertyStep;
    private LoginStep loginStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        tableStep = new TableStep(driver);
        resourceStep = new ResourceStep(driver);
        categoryStep = new CategoryStep(driver);
        employeeStep = new EmployeeStep(driver);
        propertyStep = new PropertyStep(driver);
        loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create resource and share it with user")
    public void createResourceShareItWithUser() {
        String categoryName = randomAlphabetic(MAX_CATEGORY_LENGTH / 3);
        String propertyName = randomAlphabetic(MAX_PROPERTY_LENGTH / 3);
        String user = randomAlphabetic(MAX_USER_NAME_LENGTH);
        categoryStep.createCategoryInDB(categoryName);
        propertyStep.createPropertyInDB(propertyName);
        employeeStep.createUser(user, VALID_PASSWORD);

        resourceStep.openResourcePage();
        String resourceUsername = randomAlphabetic(MAX_RESOURCE_USERNAME_LENGTH / 3);
        resourceStep.createResource(resourceUsername, categoryName, RESOURCE_SHARED_TYPE, propertyName);
        tableStep.searchInTable(resourceUsername);
        resourceStep.shareResourceWithUser(user);

        loginStep.logout();
        loginStep.login(user, VALID_PASSWORD);
        tableStep.searchInTable(resourceUsername);
        assertTrue(tableStep.isValueDisplayInTable(resourceUsername));

        cleanDB(resourceUsername, categoryName, propertyName);
        employeeStep.deleteUserInDB(user);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "smoke test", description = "Create individual resource, verify status on front and in the database")
    public void createIndividualResource() {
        String categoryName = randomAlphabetic(MAX_CATEGORY_LENGTH / 3);
        String propertyName = randomAlphabetic(MAX_PROPERTY_LENGTH / 3);
        categoryStep.createCategoryInDB(categoryName);
        propertyStep.createPropertyInDB(propertyName);

        resourceStep.openResourcePage();
        String resourceUsername = randomAlphabetic(MAX_RESOURCE_USERNAME_LENGTH / 3);
        resourceStep.createResource(resourceUsername, categoryName, "Individual", propertyName);
        tableStep.searchInTable(resourceUsername);
        assertFalse(tableStep.isValueDisplayInTable(RESOURCE_SHARED_TYPE));

        cleanDB(resourceUsername, categoryName, propertyName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "smoke test", description = "Try to delete category and property which used in resource")
    public void tryToDeleteCategoryAndPropertyUsedInResource() {
        String category = randomAlphabetic(MAX_CATEGORY_LENGTH / 3);
        String property = randomAlphabetic(MAX_PROPERTY_LENGTH / 3);
        String username = randomAlphabetic(MAX_RESOURCE_USERNAME_LENGTH);
        categoryStep.createCategoryInDB(category);
        propertyStep.createPropertyInDB(property);
        String categoryId = categoryStep.getCategoryValueInDB(category, "Id");
        String propId = propertyStep.getPropertyValueInDB(property, "Id");
        resourceStep.createResourceInDB(username, categoryId, propId);

        categoryStep.openCategoryPage();
        tableStep.searchInTable(category);
        categoryStep.deleteCategory();
        assertTrue(categoryStep.isServerErrorDisplayed());

        propertyStep.openPropertyPage();
        tableStep.searchInTable(property);
        propertyStep.deleteProperty();
        assertTrue(propertyStep.isServerErrorDisplayed());

        cleanDB(username, category, property);
    }

    private void cleanDB(String username, String category, String property){
        resourceStep.deleteResourceInDB(username);
        categoryStep.deleteCategoryInDB(category);
        propertyStep.deletePropertyInDB(property);
    }
}
