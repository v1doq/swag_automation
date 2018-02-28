package common;

import org.testng.annotations.AfterSuite;
import org.testng.annotations.BeforeSuite;
import projects.com.swag.screening.steps.candidate.CandidateStep;
import projects.com.swag.screening.steps.candidate.ScreeningStep;
import projects.com.swag.screening.steps.problem.ProblemStep;

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
