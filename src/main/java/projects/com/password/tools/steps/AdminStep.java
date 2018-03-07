package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.AdminComponent;
import projects.com.password.tools.components.TableComponent;
import settings.SQLConnector;

import static common.DefaultConstant.PASSWORD_PASS_TOOLS;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOf;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class AdminStep {

    private AdminComponent component;
    private TableComponent tableComponent;
    private static final int MIN_TITLE_LENGTH = 1;
    private String email = randomAlphabetic(9) + "@" + randomAlphabetic(2) + "." + randomAlphabetic(2);

    public AdminStep(WebDriver driver) {
        this.component = new AdminComponent(driver);
        this.tableComponent = new TableComponent(driver);
    }

    @Step("Open admin page")
    public void openAdminPage() {
        component.open(getProperty("password.tools.url") + "dashboard/administrators/");
    }

    @Step("Create new admin")
    public void createAdmin(String username, String pass){
        tableComponent.getCreateButton().click();
        component.getNameInput().sendKeys(username);
        component.getUsernameInput().sendKeys(username);
        component.getTitleInput().sendKeys(randomAlphabetic(MIN_TITLE_LENGTH));
        component.getEmailInput().sendKeys(email);
        component.getPasswordInput().sendKeys(pass);
        component.getRepeatPasswordInput().sendKeys(pass);
        component.getNextButton().click();
        component.getYourPasswordInput().sendKeys(PASSWORD_PASS_TOOLS);
        component.getCreateButton().click();
        component.assertThat(invisibilityOf(component.getYourPasswordInput()));
    }

    @Step("Delete the admin in the database")
    public void deleteAdminInDB(String username) {
        LOG.info("Try to delete admin in the database with username: " + username);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM PasswordsTool.dbo.Users WHERE UserName='" + username + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }

    @Step("Verify that admin is saved in DB with the appropriate status")
    public int getIntValueInDB(String username, String columnName){
        LOG.info("Get user's value in the database for: " + username + " in column: " + columnName);
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM PasswordsTool.dbo.Users WHERE UserName='" + username + "'";
        return connector.getIntValueInDB(query, columnName);
    }
}
