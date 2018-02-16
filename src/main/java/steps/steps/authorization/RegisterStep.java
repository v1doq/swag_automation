package steps.steps.authorization;

import settings.SQLConnector;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import steps.components.authorization.RegisterComponent;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class RegisterStep {

    private RegisterComponent component;
    private static final int MIN_NAME_LENGTH = 2;

    public RegisterStep(WebDriver driver){
        this.component = new RegisterComponent(driver);
    }

    @Step("Open landing page")
    public void openLandingPage() {
        component.open(getProperty("screening.url"));
    }

    @Step("Open register pop up, fill all fields and submit form")
    public void createNewUser(String email, String pass){
        component.openRegistrationPopUp();
        component.getFirstNameInput().sendKeys(randomAlphabetic(MIN_NAME_LENGTH));
        component.getLastNameInput().sendKeys(randomAlphabetic(MIN_NAME_LENGTH));
        component.getEmailInput().sendKeys(email);
        component.getPasswordInput().sendKeys(pass);
        component.getRepeatPasswordInput().sendKeys(pass);
        component.focusAndClick(component.getSubmitButton());
    }

    @Step("Check that user was created in database")
    public String getUsernameInDB(String email){
        LOG.info("Get username in database with email: " + email);
        SQLConnector connector = new SQLConnector();
        String name = null;
        try {
            ResultSet result = connector.
                    executeSelectQuery("SELECT * FROM SwagScreening.dbo.Users WHERE UserName = '" + email + "'");
            result.next();
            name = result.getString("UserName");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.closeConnection();
        return name;
    }

    public void deleteUserInDB(String email) {
        LOG.info("Delete user in database with email: " + email);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM SwagScreening.dbo.Users WHERE UserName = '" + email + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }
}
