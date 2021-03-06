package projects.com.communication.tool.user;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.user.LoginStepCT;
import projects.com.communication.tool.steps.user.UserStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;
import static projects.com.communication.tool.steps.user.UserStep.MIN_PASSWORD_LENGTH;

@Feature("User Management")
@Story("Functional tests for user management")
public class UserTest extends BaseTest {

    private UserStep userStep;
    private LoginStepCT loginStep;

    @BeforeMethod(description = "Authorization with token and cookies", alwaysRun = true)
    public void setUp() {
        userStep = new UserStep(driver);
        loginStep = new LoginStepCT(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create user and authorized with credential")
    public void createUser() {
        String email = randomAlphabetic(10) + "@i.ii";
        String pass = randomAlphabetic(MIN_PASSWORD_LENGTH);
        userStep.openUsersPage();
        userStep.createUser(email, pass);
        loginStep.logout();
        loginStep.login(email, pass);
        assertTrue(loginStep.isUserLogin());

        userStep.deleteUserFromDb(email);
    }
}
