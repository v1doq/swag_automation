package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.CategoryComponent;
import projects.com.password.tools.components.TableComponent;
import settings.SQLConnector;

import static org.openqa.selenium.support.ui.ExpectedConditions.elementToBeClickable;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class CategoryStep {

    private CategoryComponent component;
    private TableComponent tableComponent;
    public static final int MAX_CATEGORY_LENGTH = 50;

    public CategoryStep(WebDriver driver) {
        this.component = new CategoryComponent(driver);
        this.tableComponent = new TableComponent(driver);
    }

    @Step("Open category page")
    public void openCategoryPage() {
        component.open(getProperty("password.tools.url") + "categories/");
    }

    @Step("Create category")
    public void createCategory(String name) {
        component.assertThat(elementToBeClickable(tableComponent.getCreateButton()));
        tableComponent.getCreateButton().click();
        component.getNameInput().sendKeys(name);
        component.getOkCreateButton().click();
    }

    @Step("Update category")
    public void updateCategory(String newName) {
        component.getEditButton().click();
        component.clearAndSendKeys(component.getEditNameInput(), newName);
        component.getOkEditButton().click();
    }

    @Step("Delete category")
    public void deleteCategory() {
        component.getDeleteButton().click();
        component.jsClick(tableComponent.getYesButton());
    }

    @Step("Create a category in the database")
    public void createCategoryInDB(String name){
        LOG.info("Try to create category in the database with name: " + name);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @Id uniqueidentifier SET @Id = NEWID()\n" +
                "INSERT INTO PasswordsTool.dbo.ResourceCategories (Id, Description, Name)\n" +
                "VALUES(@Id, 'desc', '" + name + "');");
        connector.closeConnection();
        LOG.info("Successfully created");
    }

    @Step("Delete the category in the database")
    public void deleteCategoryInDB(String name) {
        LOG.info("Try to delete category in the database with name: " + name);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM PasswordsTool.dbo.ResourceCategories WHERE Name='" + name + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }

    @Step("Verify that category is saved in DB")
    public String getCategoryNameInDB(String name){
        LOG.info("Get category name in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM PasswordsTool.dbo.ResourceCategories WHERE Name='" + name + "'";
        return connector.getStringValueInDB(query, "Name");
    }
}
