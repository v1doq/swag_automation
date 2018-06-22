package projects.com.screening.candidate;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.screening.steps.candidate.CandidateFlowStep;
import projects.com.screening.steps.candidate.CandidateStep;
import projects.com.screening.steps.candidate.ScreeningStep;
import projects.com.screening.steps.problem.ProblemStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;
import static projects.com.screening.steps.problem.ProblemStep.MIN_PROBLEM_NAME_LENGTH;

@Feature("Candidate's flow")
@Story("Functional tests for candidate's flow")
public class CandidateFlowTest extends BaseTest {

    private CandidateStep candidateStep;
    private CandidateFlowStep candidateFlowStep;
    private ScreeningStep screeningStep;
    private ProblemStep problemStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        candidateStep = new CandidateStep(driver);
        candidateFlowStep = new CandidateFlowStep(driver);
        screeningStep = new ScreeningStep(driver);
        problemStep = new ProblemStep(driver);
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Confirm email by candidate, solve the task and submit screening")
    public void confirmEmailSolveTheTaskAndSubmit() {
        String email = randomAlphabetic(10) + "@" + randomAlphabetic(2) + "." + randomAlphabetic(2);
        String problemName = randomAlphabetic(MIN_PROBLEM_NAME_LENGTH);

        problemStep.createProblemWithTestCaseInDB(problemName);
        candidateStep.createCandidateInDB(email);
        String problemIdInDB = problemStep.getProblemIdInDB(problemName);
        String candidateIdInDB = candidateStep.getCandidateIdInDB(email);
        screeningStep.createScreeningInDB(candidateIdInDB, problemIdInDB);

        candidateFlowStep.goToWelcomeScreen();
        candidateFlowStep.emailConfirmation(email);
        assertTrue(candidateFlowStep.isUserConfirmedEmail());

        candidateFlowStep.goToProblemScreen();
        candidateFlowStep.solveTheTask();
        assertTrue(candidateFlowStep.isUserSolveTheTask());

        screeningStep.deleteAllScreeningInDB();
        candidateStep.deleteCandidateInDB(email);
        problemStep.deleteProblemInDB(problemName);
    }
}
