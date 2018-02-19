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

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

@Feature("Registration / Authorization")
@Story("Functional tests for sign up / sign in")
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
}
