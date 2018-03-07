package projects.com.password.tools;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.password.tools.steps.EmployeeStep;
import projects.com.password.tools.steps.LoginStep;
import projects.com.password.tools.steps.TableStep;

import static common.DefaultConstant.VALID_PASSWORD;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.password.tools.steps.EmployeeStep.MAX_USER_NAME_LENGTH;
import static common.DefaultConstant.USER_ROLE;

@Feature("Employee")
@Story("Functional tests for CRUD employee")
public class EmployeeTest extends BaseTest {

    private LoginStep loginStep;
    private TableStep tableStep;
    private EmployeeStep employeeStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        employeeStep = new EmployeeStep(driver);
        loginStep = new LoginStep(driver);
        tableStep = new TableStep(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "New user creation and authorization")
    public void createNewUserAndLogin() {
        String pass = VALID_PASSWORD;
        String username = randomAlphabetic(MAX_USER_NAME_LENGTH);
        employeeStep.createUser(username, pass);

        loginStep.logout();
        loginStep.login(username, pass);
        assertTrue(loginStep.isUserLogin());

        employeeStep.deleteUserInDB(username);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Change user's data and login with new credential")
    public void changeUsernameAndPassThanLogin() {
        String username = randomAlphabetic(MAX_USER_NAME_LENGTH / 2);
        employeeStep.createUserInDB(username, USER_ROLE);

        tableStep.searchInTable(username);
        String pass = "ytatQNRO4#";
        employeeStep.changeUserPass(pass);

        String newUsername = randomAlphabetic(MAX_USER_NAME_LENGTH);
        employeeStep.changeUsername(newUsername);

        loginStep.logout();
        loginStep.login(newUsername, pass);
        assertTrue(loginStep.isUserLogin());

        employeeStep.deleteUserInDB(newUsername);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Delete user, try to login, check status in DB")
    public void deleteUserInTable() {
        String username = randomAlphabetic(MAX_USER_NAME_LENGTH / 2);
        employeeStep.createUserInDB(username, USER_ROLE);
        tableStep.searchInTable(username);
        tableStep.deleteDataInTable();

        loginStep.logout();
        loginStep.login(username, VALID_PASSWORD);
        assertTrue(loginStep.isServerErrorDisplayed());

        int status = employeeStep.getIntValueInDB(username, "IsDeleted");
        assertEquals(1, status);

        employeeStep.deleteUserInDB(username);
    }
}
