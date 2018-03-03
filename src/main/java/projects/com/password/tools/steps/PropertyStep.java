package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.PropertyComponent;
import settings.SQLConnector;

import static settings.SeleniumListener.LOG;

public class PropertyStep {

    private PropertyComponent component;
    public static final int MAX_PROPERTY_LENGTH = 50;

    public PropertyStep(WebDriver driver) {
        this.component = new PropertyComponent(driver);
    }

    @Step("Create a property in the database")
    public void createPropertyInDB(String name){
        LOG.info("Try to create property in the database with name: " + name);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @Id uniqueidentifier SET @Id = NEWID()\n" +
                "INSERT INTO PasswordsTool.dbo.ResourceProperties (Id, Description, Name)\n" +
                "VALUES(@Id, 'desc', '" + name + "');");
        connector.closeConnection();
        LOG.info("Successfully created");
    }

    @Step("Delete the property in the database")
    public void deletePropertyInDB(String name) {
        LOG.info("Try to delete property in the database with name: " + name);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM PasswordsTool.dbo.ResourceProperties WHERE Name='" + name + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }
}
