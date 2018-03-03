package projects.com.password.tools;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.password.tools.steps.CategoryStep;
import projects.com.password.tools.steps.LoginStep;
import projects.com.password.tools.steps.TableStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static projects.com.password.tools.steps.CategoryStep.MAX_CATEGORY_LENGTH;

@Feature("Category")
@Story("Functional tests for CRUD category")
public class CategoryTest extends BaseTest {

    private TableStep tableStep;
    private CategoryStep categoryStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        tableStep = new TableStep(driver);
        categoryStep = new CategoryStep(driver);
        LoginStep loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create, update and delete a category")
    public void createUpdateDeleteCategory() {
        String categoryName = randomAlphabetic(MAX_CATEGORY_LENGTH / 3);
        categoryStep.openCategoryPage();
        categoryStep.createCategory(categoryName);
        String nameInDB = categoryStep.getCategoryNameInDB(categoryName);
        assertEquals(categoryName, nameInDB);

        tableStep.searchInTable(categoryName);
        String newCategoryName = randomAlphabetic(MAX_CATEGORY_LENGTH / 3);
        categoryStep.updateCategory(newCategoryName);
        String newNameInDB = categoryStep.getCategoryNameInDB(newCategoryName);
        assertEquals(newCategoryName, newNameInDB);

        tableStep.searchInTable(newCategoryName);
        categoryStep.deleteCategory();
        assertFalse(tableStep.isValueDisplayInTable(newCategoryName));
    }
}
