package projects.com.screening.problem;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.screening.steps.authorization.LoginStep;
import projects.com.screening.steps.problem.ProblemStep;
import projects.com.screening.steps.problem.TestCaseStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static projects.com.screening.steps.problem.ProblemStep.MIN_PROBLEM_NAME_LENGTH;

@Feature("Problem")
@Story("Functional tests for add problem and test case to a problem")
public class ProblemTest extends BaseTest {

    private ProblemStep problemStep;
    private TestCaseStep testCaseStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        problemStep = new ProblemStep(driver);
        testCaseStep = new TestCaseStep(driver);
        LoginStep loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Feature("Problem / Test case")
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create a problem with test case and verify it in database")
    public void createNewProblemAddTestCaseAndVerifyItInDB() {
        String problemName = randomAlphabetic(MIN_PROBLEM_NAME_LENGTH);
        problemStep.openProblemPage();
        problemStep.createProblem(problemName);
        String problemNameInDB = problemStep.getProblemNameInDB(problemName);
        String problemIdInDB = problemStep.getProblemIdInDB(problemName);
        assertEquals(problemName, problemNameInDB);

        problemStep.findProblemInList(problemName);
        testCaseStep.createTestCase();
        String problemIdInTestCaseTableInDB = testCaseStep.getProblemIdInTestCaseTableInDB(problemIdInDB);
        assertEquals(problemIdInDB, problemIdInTestCaseTableInDB);

        problemStep.deleteProblemInDB(problemName);
    }
}
