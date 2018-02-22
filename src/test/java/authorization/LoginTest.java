package authorization;

import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import steps.steps.authorization.LoginStep;

import static common.DefaultConstant.*;
import static common.TestData.INVALID_EMAIL_LIST;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;
import static steps.steps.authorization.RegisterStep.MIN_PASSWORD_LENGTH;

@Feature("Authorization")
@Story("Functional tests for authorization form")
public class LoginTest extends BaseTest {

    private LoginStep loginStep;

    @BeforeMethod(description = "Configuration", alwaysRun = true)
    public void init() {
        loginStep = new LoginStep(driver);
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "auth positive", description = "Authorization with space before and after credential")
    public void authorizationWithSpaceBeforeAndAfterUsernameAndPass() {
        loginStep.openLandingPage();
        loginStep.openLoginPopUp();
        loginStep.fillUsernameField(" " + USERNAME + " ");
        loginStep.fillPasswordField(" " + PASSWORD + " ");
        loginStep.submitForm();

        assertTrue(loginStep.isLogoutButtonDisplayed());
    }

    @Severity(SeverityLevel.MINOR)
    @Test(groups = "auth positive", description = "Authorization with username in uppercase")
    public void authorizationWithUsernameInUppercase() {
        loginStep.openLandingPage();
        loginStep.openLoginPopUp();
        loginStep.fillUsernameField(USERNAME.toUpperCase());
        loginStep.fillPasswordField(PASSWORD);
        loginStep.submitForm();

        assertTrue(loginStep.isLogoutButtonDisplayed());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "auth negative", description = "Try to login with invalid pass")
    public void submitWithIncorrectPass() {
        loginStep.openLandingPage();
        loginStep.openLoginPopUp();
        loginStep.fillUsernameField(USERNAME);
        loginStep.fillPasswordField(randomAlphabetic(MIN_PASSWORD_LENGTH));
        loginStep.submitForm();

        assertTrue(loginStep.isServerErrorDisplayed());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "auth negative", description = "Try to login with invalid username")
    public void submitWithInvalidUsername() {
        String userName = randomAlphabetic(10) + "@" + randomAlphabetic(2) + "." + randomAlphabetic(2);
        loginStep.openLandingPage();
        loginStep.openLoginPopUp();
        loginStep.fillUsernameField(userName);
        loginStep.fillPasswordField(PASSWORD);
        loginStep.submitForm();

        assertTrue(loginStep.isServerErrorDisplayed());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "auth negative", description = "Check validation messages for empty fields")
    public void submitWithEmptyFields() {
        loginStep.openLandingPage();
        loginStep.openLoginPopUp();
        loginStep.submitForm();

        assertTrue(loginStep.isUsernameErrorDisplayed());
        assertTrue(loginStep.isPasswordErrorDisplayed());
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "auth negative", description = "Check validation messages for email field")
    public void submitWithInvalidEmail() {
        loginStep.openLandingPage();
        loginStep.openLoginPopUp();

        for (String email : INVALID_EMAIL_LIST) {
            loginStep.fillUsernameField(email);
            loginStep.submitForm();

            assertTrue(loginStep.isUsernameErrorDisplayed());
        }
    }

    @Severity(SeverityLevel.NORMAL)
    @Test(groups = "auth negative", description = "Check validation messages for password field")
    public void submitWithInvalidPass() {
        loginStep.openLandingPage();
        loginStep.openLoginPopUp();

        for (String pass : loginStep.invalidPassList) {
            loginStep.fillPasswordField(pass);
            loginStep.submitForm();

            assertTrue(loginStep.isPasswordErrorDisplayed());
        }
    }
}
