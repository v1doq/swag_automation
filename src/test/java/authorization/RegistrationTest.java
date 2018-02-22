package authorization;

import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import steps.steps.authorization.LoginStep;
import steps.steps.authorization.RegisterStep;

import static common.DefaultConstant.*;
import static common.TestData.*;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.*;
import static steps.steps.authorization.RegisterStep.MIN_PASSWORD_LENGTH;

@Feature("Registration")
@Story("Functional tests for registration form")
public class RegistrationTest extends BaseTest {

    private LoginStep loginStep;
    private RegisterStep registerStep;

    @BeforeMethod(description = "Configuration", alwaysRun = true)
    public void init() {
        loginStep = new LoginStep(driver);
        registerStep = new RegisterStep(driver);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "New user registration and authorization")
    public void registerNewUserAndLogin() {
        String userName = randomAlphabetic(10) + "@" + randomAlphabetic(2) + "." + randomAlphabetic(2);
        String pass = "Qqqqqqq1";

        registerStep.openLandingPage();
        registerStep.createNewUser(userName, pass);
        String userNameInDB = registerStep.getUsernameInDB(userName);
        assertEquals(userName, userNameInDB);

        loginStep.logout();
        loginStep.login(userName, pass);
        assertTrue(loginStep.isLogoutButtonDisplayed());

        registerStep.deleteUserInDB(userName);
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register positive", description = "Input to first and last name fields valid values")
    public void submitWithValidValuesInNameFields() {
        registerStep.openLandingPage();
        registerStep.openRegistrationPopUp();

        for (String name : VALID_NAME_LIST) {
            registerStep.fillNamesFields(name);
            registerStep.submitForm();

            assertFalse(registerStep.isFirstNameErrorDisplayed());
            assertFalse(registerStep.isLastNameErrorDisplayed());
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register positive", description = "Input to email field valid values")
    public void submitWithValidEmail() {
        registerStep.openLandingPage();
        registerStep.openRegistrationPopUp();

        for (String email : VALID_EMAIL_LIST) {
            registerStep.fillEmailField(email);
            registerStep.submitForm();

            assertFalse(registerStep.isEmailErrorDisplayed());
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register positive", description = "Input to password fields valid values")
    public void submitWithValidPassword() {
        registerStep.openLandingPage();
        registerStep.openRegistrationPopUp();

        for (String pass : registerStep.validPassList) {
            registerStep.fillPasswordField(pass);
            registerStep.fillRepeatPasswordField(pass);
            registerStep.submitForm();

            assertFalse(registerStep.isPasswordErrorDisplayed());
            assertFalse(registerStep.isRepeatPasswordErrorDisplayed());
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register negative", description = "Check validation messages for first and last name fields")
    public void submitWithInvalidValuesInNameFields() {
        registerStep.openLandingPage();
        registerStep.openRegistrationPopUp();

        for (String name : INVALID_NAME_LIST) {
            registerStep.fillNamesFields(name);
            registerStep.submitForm();

            assertTrue(registerStep.isFirstNameErrorDisplayed());
            assertTrue(registerStep.isLastNameErrorDisplayed());
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register negative", description = "Check validation messages for email field")
    public void submitWithInvalidEmail() {
        registerStep.openLandingPage();
        registerStep.openRegistrationPopUp();

        for (String email : INVALID_EMAIL_LIST) {
            registerStep.fillEmailField(email);
            registerStep.submitForm();

            assertTrue(registerStep.isEmailErrorDisplayed());
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register negative", description = "Try to create user with existing email")
    public void duplicateEmail() {
        registerStep.openLandingPage();
        registerStep.createNewUser(USERNAME, PASSWORD);

        assertTrue(registerStep.checkServerError());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register negative", description = "Check validation messages for password field")
    public void submitWithInvalidPassword() {
        registerStep.openLandingPage();
        registerStep.openRegistrationPopUp();

        for (String pass : registerStep.invalidPassList) {
            registerStep.fillPasswordField(pass);
            registerStep.submitForm();

            assertTrue(registerStep.isPasswordErrorDisplayed());
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register negative", description = "Check validation messages for mismatch passwords")
    public void mismatchPasswords() {
        registerStep.openLandingPage();
        registerStep.openRegistrationPopUp();
        registerStep.fillPasswordField(randomAlphabetic(MIN_PASSWORD_LENGTH));
        registerStep.fillRepeatPasswordField(randomAlphabetic(MIN_PASSWORD_LENGTH));
        registerStep.submitForm();

        assertTrue(registerStep.isRepeatPasswordErrorDisplayed());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "register negative", description = "Check validation messages for empty fields")
    public void submitWithEmptyFields() {
        registerStep.openLandingPage();
        registerStep.openRegistrationPopUp();
        registerStep.submitForm();

        assertTrue(registerStep.isFirstNameErrorDisplayed());
        assertTrue(registerStep.isLastNameErrorDisplayed());
        assertTrue(registerStep.isEmailErrorDisplayed());
        assertTrue(registerStep.isPasswordErrorDisplayed());
        assertTrue(registerStep.isRepeatPasswordErrorDisplayed());
    }
}
