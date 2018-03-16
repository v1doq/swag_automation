package projects.com.password.tools;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.password.tools.steps.AdminStep;
import projects.com.password.tools.steps.LoginStep;
import projects.com.password.tools.steps.ProfileStep;

import static common.DefaultConstant.ADMIN_ROLE;
import static common.DefaultConstant.VALID_PASSWORD;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.password.tools.steps.EmployeeStep.MAX_USER_NAME_LENGTH;

@Feature("User's profile")
@Story("Functional tests for user profile")
public class ProfileTest extends BaseTest {

    private ProfileStep profileStep;
    private LoginStep loginStep;
    private AdminStep adminStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        profileStep = new ProfileStep(driver);
        adminStep = new AdminStep(driver);
        loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create admin and edit profile, login with old and new credential")
    public void createAdminAndEditProfile() {
        String username = randomAlphabetic(MAX_USER_NAME_LENGTH);
        String userPass = VALID_PASSWORD;

        adminStep.openAdminPage();
        adminStep.createAdmin(username, userPass);
        loginStep.logout();
        loginStep.login(username, userPass);
        int isAdmin = adminStep.getIntValueInDB(username, "IsAdmin");
        assertEquals(ADMIN_ROLE, isAdmin);

        String newUsername = randomAlphabetic(MAX_USER_NAME_LENGTH);
        String newPass = "JEkjmq_IX7";
        profileStep.goToProfilePopUp();
        profileStep.editUsernameAndPass(newUsername, userPass, newPass);

        loginStep.logout();
        loginStep.login(username, userPass);
        assertTrue(loginStep.isServerErrorDisplayed());
        refreshPage();

        loginStep.login(newUsername, newPass);
        assertTrue(loginStep.isUserLogin());

        adminStep.deleteAdminInDB(username);
        adminStep.deleteAdminInDB(newUsername);
    }
}
