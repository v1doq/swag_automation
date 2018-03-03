package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.EmployeeComponent;
import projects.com.password.tools.components.TableComponent;
import settings.SQLConnector;

import static common.ConciseApi.clearAndType;
import static common.DefaultConstant.PASSWORD_HASH;
import static common.DefaultConstant.PASSWORD_PASS_TOOLS;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static settings.SeleniumListener.LOG;

public class EmployeeStep {

    private EmployeeComponent component;
    private TableComponent tableComponent;
    private static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_EMPLOYEE_NAME_LENGTH = 50;
    private static final int MIN_TITLE_LENGTH = 1;
    private String email = randomAlphabetic(9) + "@" + randomAlphabetic(2) + "." + randomAlphabetic(2);

    public EmployeeStep(WebDriver driver) {
        this.component = new EmployeeComponent(driver);
        this.tableComponent = new TableComponent(driver);
    }

    @Step("Create new user")
    public void createUser(String username, String pass){
        tableComponent.getCreateButton().click();
        component.getNameInput().sendKeys(randomAlphabetic(MIN_NAME_LENGTH));
        component.getUsernameInput().sendKeys(username);
        component.getTitleInput().sendKeys(randomAlphabetic(MIN_TITLE_LENGTH));
        component.getEmailInput().sendKeys(email);
        component.getPasswordInput().sendKeys(pass);
        component.getSubmitButton().click();
    }

    @Step("Change user's password")
    public void changeUserPass(String pass){
        tableComponent.getActionButton().click();
        component.getYourPasswordInput().sendKeys(PASSWORD_PASS_TOOLS);
        component.getNewPasswordInput().sendKeys(pass);
        component.getOkButton().click();
    }

    @Step("Change user's username")
    public void changeUsername(String username){
        tableComponent.jsClick(tableComponent.getEditButton());
        component.getProfileTab().click();
        clearAndType(component.getEditUsernameInput(), username);
        component.getUpdateButton().click();
    }

    @Step("Create user in the database")
    public void createUserInDB(String username, int userRole){
        LOG.info("Try to create user in the database with username: " + username);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @Id uniqueidentifier SET @Id = NEWID()\n" +
                "INSERT INTO PasswordsTool.dbo.Users\n" +
                "(Id, AccessFailedCount, Email, EmailConfirmed, IsAdmin, IsDeleted, LockoutEnabled, Name, " +
                "PasswordHash, PhoneNumberConfirmed, Title, TwoFactorEnabled, UserName, DeletionToken)\n" +
                "VALUES(@Id, 0, '" + email + "', 0, " + userRole + ", 0, 0, 'name', " +
                "'" + PASSWORD_HASH + "', 0, 'title', 0, '" + username + "', '');");
        //PASSWORD_HASH is hash of VALID_PASSWORD(frDPgoZ#Y5)
        connector.closeConnection();
        LOG.info("Successfully created");
    }

    @Step("Create user in the database")
    public void createUserInDB(String username, String name, int userRole){
        LOG.info("Try to create user in the database with username: " + username);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @Id uniqueidentifier SET @Id = NEWID()\n" +
                "INSERT INTO PasswordsTool.dbo.Users\n" +
                "(Id, AccessFailedCount, Email, EmailConfirmed, IsAdmin, IsDeleted, LockoutEnabled, Name, " +
                "PasswordHash, PhoneNumberConfirmed, Title, TwoFactorEnabled, UserName, DeletionToken)\n" +
                "VALUES(@Id, 0, '" + email + "', 0, " + userRole + ", 0, 0, '" + name + "', " +
                "'" + PASSWORD_HASH + "', 0, 'title', 0, '" + username + "', '');");
        //PASSWORD_HASH is hash of VALID_PASSWORD(frDPgoZ#Y5)
        connector.closeConnection();
        LOG.info("Successfully created");
    }

    @Step("Delete the user in the database")
    public void deleteUserInDB(String username) {
        LOG.info("Try to delete user in the database with username: " + username);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM PasswordsTool.dbo.Users WHERE UserName='" + username + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }

    @Step("Verify that user is saved in DB with the appropriate status")
    public int getIsDeletedUserStatusInDB(String username){
        LOG.info("Get user's isDeleted status in the database with email: " + username);
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM PasswordsTool.dbo.Users WHERE UserName='" + username + "'";
        return connector.getIntValueInDB(query, "IsDeleted");
    }
}
