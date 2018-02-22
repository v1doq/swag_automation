package steps.steps.candidate;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import steps.components.candidate.CandidateFlowComponent;

import static common.DefaultConstant.CANDIDATE_ACCESS_CODE;
import static settings.TestConfig.getProperty;

public class CandidateFlowStep {

    private CandidateFlowComponent component;

    public CandidateFlowStep(WebDriver driver){
        this.component = new CandidateFlowComponent(driver);
    }

    @Step("Open candidate's welcome page by assigned URL")
    public void goToWelcomeScreen(){
        component.open(getProperty("screening.url") + "screening/" + CANDIDATE_ACCESS_CODE);
    }

    @Step("Confirm candidate's email")
    public void emailConfirmation(String email){
        component.getEmailInput().sendKeys(email);
        component.getConfirmButton().click();
    }

    @Step("Verify that candidate confirm his email")
    public boolean isUserConfirmedEmail(){
        String text = "Assignment details";
        return component.getH1(text).getText().contains(text);
    }

    @Step("Go to problem screen")
    public void goToProblemScreen(){
        component.getStartButton().click();
    }

    @Step("Input solution, run compiler, verify success status and submit form")
    public void solveTheTask(){
        component.getLanguageDropdown().click();
        component.getLanguageItem().click();
        component.getSolutionInput().sendKeys("public class test { public static void main(String[] args) " +
                "{ System.out.println(\"Hello world\"); } }");
        component.getRunButton().click();
        component.waitForCompilerResponse("Test run result: Ok");
        component.getSubmitButton().click();
        component.getH1("Congratulations!");
    }

    @Step("Verify that candidate go to finish screening step")
    public boolean isUserSolveTheTask(){
        String text = "Congratulations!";
        return component.getH1(text).getText().contains(text);
    }
}
