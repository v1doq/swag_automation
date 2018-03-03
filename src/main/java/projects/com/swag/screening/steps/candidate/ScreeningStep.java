package projects.com.swag.screening.steps.candidate;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.swag.screening.components.candidate.ScreeningComponent;
import settings.SQLConnector;

import static common.DefaultConstant.CANDIDATE_ACCESS_CODE;
import static common.DefaultConstant.MANAGER_ID;
import static settings.SeleniumListener.LOG;

public class ScreeningStep {

    private ScreeningComponent component;

    public ScreeningStep(WebDriver driver){
        this.component = new ScreeningComponent(driver);
    }

    @Step("Create screening")
    public void createScreening(){
        component.openScreeningPopUp();
        component.jsClick(component.getLanguageCheckbox());
        component.getProblemCheckbox().click();
        component.getSubmitButton().click();
    }

    @Step("Get candidate's assigned URL and compare it with candidate access code")
    public String getAssignedUrl(){
        component.getResultsButton().click();
        return component.getAssignedUrlValue().getText();
    }

    @Step("Create a screening in the database")
    public void createScreeningInDB(String candidateId, String problemId){
        LOG.info("Create a screening in the database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @screeningId uniqueidentifier\n" +
                "SET @screeningId = NEWID()\n" +
                "INSERT INTO SwagScreening.dbo.Screenings\n" +
                "(Id, CandidateAccessCode, CandidateId, ManagerId, Status)\n" +
                "VALUES(@screeningId, '" + CANDIDATE_ACCESS_CODE + "', '" + candidateId + "', '" + MANAGER_ID + "', 1);" +
                "DECLARE @screeningProblemId uniqueidentifier\n" +
                "SET @screeningProblemId = NEWID()\n" +
                "INSERT INTO SwagScreening.dbo.ScreeningProblems\n" +
                "(Id, ProblemId, ScreeningId, SubmitId)\n" +
                "VALUES(@screeningProblemId, '" + problemId + "', @screeningId, NULL);" +
                "DECLARE @screeningProblemLanguageId uniqueidentifier\n" +
                "SET @screeningProblemLanguageId = NEWID()\n" +
                "INSERT INTO SwagScreening.dbo.ScreeningProblemLanguage\n" +
                "(Id, LanguageId, ScreeningProblemId)\n" +
                "VALUES(@screeningProblemLanguageId, 'CBE84B9C-7DDA-4A2A-6F06-08D5619AAC8A', @screeningProblemId);");
        connector.closeConnection();
    }

    @Step("Check that screening was created in database and it is linked to the candidate")
    public String getCandidateIdInScreeningTableInDB(String candidateId){
        LOG.info("Select screening in database with candidate ID: " + candidateId);
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM SwagScreening.dbo.Screenings WHERE CandidateId = '" + candidateId + "'";
        return connector.getStringValueInDB(query, "CandidateId");
    }

    public String getCandidateAccessCodeInScreeningTableInDB(String candidateId){
        LOG.info("Get candidate's access code in screening table in database: " + candidateId);
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM SwagScreening.dbo.Screenings WHERE CandidateId = '" + candidateId + "'";
        return connector.getStringValueInDB(query, "CandidateAccessCode");
    }

    @Step("Delete the screening in the database")
    public void deleteScreeningInDB(String candidateId) {
        LOG.info("Delete screening in the database with candidateId: " + candidateId);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM SwagScreening.dbo.Screenings WHERE CandidateId = '" + candidateId + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }

    @Step("Delete all screening in the database")
    public void deleteAllScreeningInDB() {
        LOG.info("Delete all screening in database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM SwagScreening.dbo.Screenings");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }
}
