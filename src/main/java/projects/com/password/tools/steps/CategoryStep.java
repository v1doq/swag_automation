package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.CategoryComponent;
import settings.SQLConnector;

import static settings.SeleniumListener.LOG;

public class CategoryStep {

    private CategoryComponent component;
    public static final int MAX_CATEGORY_LENGTH = 50;

    public CategoryStep(WebDriver driver) {
        this.component = new CategoryComponent(driver);
    }

    @Step("Create user in the database")
    public void createCategoryInDB(String name){
        LOG.info("Try to create category in the database with name: " + name);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @Id uniqueidentifier SET @Id = NEWID()\n" +
                "INSERT INTO PasswordsTool.dbo.ResourceCategories (Id, Description, Name)\n" +
                "VALUES(@Id, 'autotest', '" + name + "');");
        connector.closeConnection();
        LOG.info("Successfully created");
    }
}
