package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.PropertyComponent;
import projects.com.password.tools.components.TableComponent;
import settings.SQLConnector;

import static org.openqa.selenium.By.cssSelector;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class PropertyStep {

    private PropertyComponent component;
    private TableComponent tableComponent;
    public static final int MAX_PROPERTY_LENGTH = 50;

    public PropertyStep(WebDriver driver) {
        this.component = new PropertyComponent(driver);
        this.tableComponent = new TableComponent(driver);
    }

    @Step("Open property page")
    public void openPropertyPage() {
        component.open(getProperty("password.tools.url") + "properties/");
    }

    @Step("Create property")
    public void createProperty(String name) {
        tableComponent.getCreateButton().click();
        component.getNameInput().sendKeys(name);
        component.getOkButton().click();
    }

    @Step("Update property")
    public void updateProperty(String newName) {
        component.getEditButton().click();
        component.clearAndType(component.getEditNameInput(), newName);
        component.getOkButton().click();
    }

    @Step("Delete property")
    public void deleteProperty() {
        component.getDeleteButton().click();
        component.waitForPartOfText(cssSelector(".card__text"), "Are you sure to delete");
        component.jsClick(tableComponent.getYesButton());
        component.assertThat(invisibilityOfElementLocated(cssSelector(".card__text")));
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

    @Step("Verify that property is saved in DB")
    public String getPropertyNameInDB(String name){
        LOG.info("Get property name in the database");
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM PasswordsTool.dbo.ResourceProperties WHERE Name='" + name + "'";
        return connector.getStringValueInDB(query, "Name");
    }
}
