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
import static org.testng.Assert.assertTrue;
import static projects.com.password.tools.steps.CategoryStep.MAX_CATEGORY_LENGTH;
import static projects.com.password.tools.steps.EmployeeStep.MAX_EMPLOYEE_NAME_LENGTH;
import static common.DefaultConstant.USER_ROLE;
import static projects.com.password.tools.steps.PropertyStep.MAX_PROPERTY_LENGTH;
import static projects.com.password.tools.steps.ResourceStep.MAX_RESOURCE_USERNAME_LENGTH;

@Feature("Resources")
@Story("Functional tests for CRUD resource")
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
        String employeeUsername = randomAlphabetic(MAX_EMPLOYEE_NAME_LENGTH);
        String employeeName = randomAlphabetic(MAX_EMPLOYEE_NAME_LENGTH / 3);
        categoryStep.createCategoryInDB(categoryName);
        propertyStep.createPropertyInDB(propertyName);
        employeeStep.createUserInDB(employeeUsername, employeeName, USER_ROLE);

        resourceStep.openResourcePage();
        String resourceUsername = randomAlphabetic(MAX_RESOURCE_USERNAME_LENGTH / 3);
        resourceStep.createResource(resourceUsername, categoryName, "Shared", propertyName);
        tableStep.searchInTable(resourceUsername);
        resourceStep.shareResourceWithUser(employeeName);

        loginStep.logout();
        loginStep.login(employeeUsername, VALID_PASSWORD);
        tableStep.searchInTable(resourceUsername);

        assertTrue(tableStep.isValueDisplayInTable(resourceUsername));

        resourceStep.deleteResourceInDB(resourceUsername);
        categoryStep.deleteCategoryInDB(categoryName);
        propertyStep.deletePropertyInDB(propertyName);
        employeeStep.deleteUserInDB(employeeUsername);
    }
}
