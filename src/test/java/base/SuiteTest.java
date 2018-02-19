package base;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import steps.steps.candidate.CandidateStep;
import steps.steps.candidate.ScreeningStep;
import steps.steps.problem.ProblemStep;

public class SuiteTest extends BaseTest {

    @BeforeSuite(description = "Clean database before suite", alwaysRun = true)
    public void setUp() {
        cleanDatabase();
    }

    @AfterSuite(description = "Clean database after suite", alwaysRun = true)
    public void tearDown() {
        cleanDatabase();
    }

    private void cleanDatabase() {
        CandidateStep candidateStep = new CandidateStep(driver);
        ScreeningStep screeningStep = new ScreeningStep(driver);
        ProblemStep problemStep = new ProblemStep(driver);
        screeningStep.deleteAllScreeningInDB();
        candidateStep.deleteAllCandidateInDB();
        problemStep.deleteAllProblemInDB();
    }
}
