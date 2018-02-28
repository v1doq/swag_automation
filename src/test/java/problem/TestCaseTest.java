package problem;

import base.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.swag.screening.steps.authorization.LoginStep;
import projects.com.swag.screening.steps.problem.ProblemStep;
import projects.com.swag.screening.steps.problem.TestCaseStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static projects.com.swag.screening.steps.problem.ProblemStep.MIN_PROBLEM_NAME_LENGTH;

@Feature("Test cases")
@Story("Functional tests for add test case to the problem")
public class TestCaseTest extends BaseTest {

    private ProblemStep problemStep;
    private TestCaseStep testCaseStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        problemStep = new ProblemStep(driver);
        testCaseStep = new TestCaseStep(driver);
        LoginStep loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(description = "Create a problem, add test case and verify it in database")
    public void createNewTestCaseAndVerifyItInDB() {
        String problemName = randomAlphabetic(MIN_PROBLEM_NAME_LENGTH);
        problemStep.createProblemInDB(problemName);
        String problemIdInDB = problemStep.getProblemIdInDB(problemName);

        problemStep.openProblemPage();
        problemStep.findProblemInList(problemName);
        testCaseStep.createTestCase();
        String problemIdInTestCaseTableInDB = testCaseStep.getProblemIdInTestCaseTableInDB(problemIdInDB);
        assertEquals(problemIdInDB, problemIdInTestCaseTableInDB);

        problemStep.deleteProblemInDB(problemName);
    }
}
