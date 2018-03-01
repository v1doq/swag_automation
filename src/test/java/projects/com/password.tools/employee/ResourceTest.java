package projects.com.password.tools.employee;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.password.tools.steps.CategoryStep;
import projects.com.password.tools.steps.LoginStep;
import projects.com.password.tools.steps.ResourceStep;
import projects.com.password.tools.steps.TableStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static projects.com.password.tools.steps.CategoryStep.MAX_CATEGORY_LENGTH;

@Feature("Resources")
@Story("Functional tests for CRUD resource")
public class ResourceTest extends BaseTest {

    private TableStep tableStep;
    private ResourceStep resourceStep;
    private CategoryStep categoryStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        tableStep = new TableStep(driver);
        resourceStep = new ResourceStep(driver);
        categoryStep = new CategoryStep(driver);
        LoginStep loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "New user creation and authorization")
    public void createNewUserAndLogin() {
        String categoryName = randomAlphabetic(MAX_CATEGORY_LENGTH);
        categoryStep.createCategoryInDB(categoryName);

        resourceStep.openResourcePage();
        resourceStep.createResource(categoryName, "Shared");

    }
}
