package projects.com.screening.candidate;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.screening.steps.candidate.CandidateStep;
import projects.com.screening.steps.authorization.LoginStep;
import projects.com.screening.steps.problem.ProblemStep;
import projects.com.screening.steps.candidate.ScreeningStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;
import static projects.com.screening.steps.problem.ProblemStep.MIN_PROBLEM_NAME_LENGTH;

@Feature("Candidate")
@Story("Functional tests for add candidate and screening to a candidate")
public class CandidateTest extends BaseTest {

    private CandidateStep candidateStep;
    private ScreeningStep screeningStep;
    private ProblemStep problemStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        candidateStep = new CandidateStep(driver);
        screeningStep = new ScreeningStep(driver);
        problemStep = new ProblemStep(driver);
        LoginStep loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Feature("Candidate / Screening")
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create a candidate, add screening and verify assigned URL")
    public void createNewCandidateAndVerifyItInDB() {
        String email = randomAlphabetic(10) + "@" + randomAlphabetic(2) + "." + randomAlphabetic(2);
        String problemName = randomAlphabetic(MIN_PROBLEM_NAME_LENGTH);
        problemStep.createProblemWithTestCaseInDB(problemName);

        candidateStep.openCandidatePage();
        candidateStep.createCandidate(email);
        String candidateEmailInDB = candidateStep.getCandidateEmailInDB(email);
        assertEquals(email, candidateEmailInDB);

        candidateStep.findCandidateInList(email);
        screeningStep.createScreening();
        String candidateIdInDB = candidateStep.getCandidateIdInDB(email);
        String candidateIdInScreening = screeningStep.getCandidateIdInScreeningTableInDB(candidateIdInDB);
        assertEquals(candidateIdInDB, candidateIdInScreening);

        String candidateAccessCode = screeningStep.getCandidateAccessCodeInScreeningTableInDB(candidateIdInDB);
        String assignedUrl = screeningStep.getAssignedUrl();
        assertTrue(assignedUrl.contains(candidateAccessCode));

        screeningStep.deleteScreeningInDB(candidateIdInScreening);
        candidateStep.deleteCandidateInDB(email);
        problemStep.deleteProblemInDB(problemName);
    }
}
