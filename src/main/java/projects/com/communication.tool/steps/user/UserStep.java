package projects.com.communication.tool.steps.user;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.user.UserComponent;
import settings.SQLConnector;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.support.ui.ExpectedConditions.attributeToBe;
import static projects.com.communication.tool.common.CommStackDB.USER_DB;
import static settings.SQLConnector.*;
import static settings.TestConfig.getProperty;

public class UserStep {

    private UserComponent component;
    public static final byte MIN_PASSWORD_LENGTH = 8;

    public UserStep(WebDriver driver) {
        this.component = new UserComponent(driver);
    }

    @Step("Open users page")
    public void openUsersPage() {
        component.open(getProperty("communication.tool.url") + "users/");
    }

    @Step("Create user")
    public void createUser(String email, String pass) {
        component.getCreateUserButton().click();
        component.getFirstNameInput().sendKeys(randomAlphabetic(5));
        component.getLastNameInput().sendKeys(randomAlphabetic(5));
        component.getEmailInput().sendKeys(email);
        component.getPasswordInput().sendKeys(pass);
        component.getSubmitButton().click();
        component.assertThat(attributeToBe(className("modal-mask"), "style", "display: none;"));
    }

    @Step("Delete user from database")
    public void deleteUserFromDb(String email) {
        SQLConnector connector = new SQLConnector();
        connector.executeQuery(DELETE_FROM + USER_DB + WHERE + "Email='" + email + "'");
        connector.closeConnection();
    }
}
