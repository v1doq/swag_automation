package steps.steps.authorization;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import settings.SQLConnector;
import steps.components.authorization.RegisterComponent;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.RandomStringUtils.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;
import static steps.components.base.ConciseApi.sleep;

public class RegisterStep {

    private RegisterComponent component;

    public static final int MIN_NAME_LENGTH = 2;
    public static final int MAX_NAME_LENGTH = 64;
    public static final int MIN_PASSWORD_LENGTH = 8;
    static final int MAX_PASSWORD_LENGTH = 128;

    public RegisterStep(WebDriver driver){
        this.component = new RegisterComponent(driver);
    }

    @Step("Open landing page")
    public void openLandingPage() {
        component.open(getProperty("screening.url"));
    }

    @Step("Create new user")
    public void createNewUser(String userName, String pass){
        component.openRegistrationPopUp();
        component.getFirstNameInput().sendKeys(randomAlphabetic(MIN_NAME_LENGTH));
        component.getLastNameInput().sendKeys(randomAlphabetic(MIN_NAME_LENGTH));
        component.getEmailInput().sendKeys(userName);
        component.getPasswordInput().sendKeys(pass);
        component.getRepeatPasswordInput().sendKeys(pass);
        component.jsClick(component.getSubmitButton());
    }

    @Step("Open registration pop up")
    public void openRegistrationPopUp() {
        component.openRegistrationPopUp();
    }

    @Step("Submit registration form")
    public void submitForm() {
        component.assertThat(elementToBeClickable(component.getSubmitButton()));
        component.assertThat(visibilityOf(component.getSubmitButton()));
        component.actionClick(component.getSubmitButton());
        sleep(700); //need to fix
    }

    @Step("Fill first and last name fields")
    public void fillNamesFields(String name){
        component.clearAndType(component.getFirstNameInput(), name);
        component.clearAndType(component.getLastNameInput(), name);
    }

    @Step("Fill email fields")
    public void fillEmailField(String email){
        component.clearAndType(component.getEmailInput(), email);
    }

    @Step("Fill password fields")
    public void fillPasswordField(String pass){
        component.clearAndType(component.getPasswordInput(), pass);
    }

    @Step("Fill repeat password fields")
    public void fillRepeatPasswordField(String pass){
        component.clearAndType(component.getRepeatPasswordInput(), pass);
    }

    @Step("Check server error message for duplicate email")
    public boolean checkServerError(){
        String text = "User with provided email already exists";
        return component.getServerError(text).getText().contains(text);
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

    public List<String> invalidPassList = asList(
            "  ",
            "",
            randomAlphabetic(MIN_PASSWORD_LENGTH - 1),
            randomAlphabetic(MIN_PASSWORD_LENGTH).toLowerCase(),
            randomAlphabetic(MIN_PASSWORD_LENGTH).toUpperCase(),
            randomNumeric(MIN_PASSWORD_LENGTH),
            randomAlphabetic(MAX_PASSWORD_LENGTH + 1)
    );

    public List<String> validPassList = asList(
            "Qqqqqqq1",
            "Qq111111",
            "QQQQQQq1",
            "Qqq qqq1",
            "Qq1!@#$%",
            "Qq1" + randomAlphabetic(MAX_PASSWORD_LENGTH - 3)
    );

    @Step("Check for the presence of the error message for first name field")
    public boolean isFirstNameErrorDisplayed() {
        return component.isFirstNameErrorDisplayed();
    }

    @Step("Check for the presence of the error message for last name field")
    public boolean isLastNameErrorDisplayed() {
        return component.isLastNameErrorDisplayed();
    }

    @Step("Check for the presence of the error message for email field")
    public boolean isEmailErrorDisplayed() {
        return component.isEmailErrorDisplayed();
    }

    @Step("Check for the presence of the error message for password field")
    public boolean isPasswordErrorDisplayed() {
        return component.isPasswordErrorDisplayed();
    }

    @Step("Check for the presence of the error message for repeat password field")
    public boolean isRepeatPasswordErrorDisplayed() {
        return component.isRepeatPasswordErrorDisplayed();
    }
}
