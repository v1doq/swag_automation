package projects.com.password.tools.employee;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.password.tools.steps.EmployeeStep;
import projects.com.password.tools.steps.LoginStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;
import static projects.com.password.tools.steps.EmployeeStep.MAX_NAME_LENGTH;

@Feature("Employee")
@Story("Functional tests for CRUD employee")
public class EmployeeTest extends BaseTest {

    private EmployeeStep employeeStep;
    private LoginStep loginStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        employeeStep = new EmployeeStep(driver);
        loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "New user creation and authorization")
    public void createNewUserAndLogin() {
        String pass = "ktxRCLt@Q5";
        String username = randomAlphabetic(MAX_NAME_LENGTH);

        employeeStep.createUser(username, pass);
        loginStep.logout();
        loginStep.login(username, pass);

        assertTrue(loginStep.isUserLogin());
    }
}
