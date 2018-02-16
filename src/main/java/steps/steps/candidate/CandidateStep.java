package steps.steps.candidate;

import common.DefaultConstant;
import settings.SQLConnector;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import steps.components.candidate.CandidateComponent;

import java.sql.ResultSet;
import java.sql.SQLException;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class CandidateStep {

    private CandidateComponent component;
    private static final int MIN_NAME_LENGTH = 2;

    public CandidateStep(WebDriver driver){
        this.component = new CandidateComponent(driver);
    }

    @Step("Open candidate's page")
    public void openCandidatePage() {
        component.open(getProperty("screening.url") + "dashboard/");
    }

    @Step("Create a candidate")
    public void createCandidate(String email){
        component.openCandidatePopUp();
        component.getFirstNameInput().sendKeys(randomAlphabetic(MIN_NAME_LENGTH));
        component.getLastNameInput().sendKeys(randomAlphabetic(MIN_NAME_LENGTH));
        component.getEmailInput().sendKeys(email);
        component.getSubmitButton().click();
    }

    public void findCandidateInList(String candidateEmail){
        component.getSearchField().sendKeys(candidateEmail);
        component.waitForSearchResult(candidateEmail);
    }

    @Step("Check that candidate was created in database")
    public String getCandidateEmailInDB(String email){
        LOG.info("Get candidate in the database with email: " + email);
        SQLConnector connector = new SQLConnector();
        String name = null;
        try {
            ResultSet result = connector.
                    executeSelectQuery("SELECT * FROM SwagScreening.dbo.Candidates WHERE Email = '" + email + "'");
            result.next();
            name = result.getString("Email");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.closeConnection();
        return name;
    }

    public String getCandidateIdInDB(String email){
        LOG.info("Get candidate's ID in the database with email: " + email);
        SQLConnector connector = new SQLConnector();
        String id = null;
        try {
            ResultSet result = connector.
                    executeSelectQuery("SELECT * FROM SwagScreening.dbo.Candidates WHERE Email = '" + email + "'");
            result.next();
            id = result.getString("Id");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.closeConnection();
        return id;
    }

    @Step("Create a candidate in the database")
    public void createCandidateInDB(String email){
        LOG.info("Create a candidate in the database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @candidateId uniqueidentifier\n" +
                "SET @candidateId = NEWID() \n" +
                "INSERT INTO SwagScreening.dbo.Candidates\n" +
                "(Id, Comment, Email, FirstName, IsActive, LastName, ManagerId)\n" +
                "VALUES(@candidateId, '', '" + email + "', 'test', 1, 'test', '" + DefaultConstant.MANAGER_ID + "');");
        connector.closeConnection();
    }

    @Step("Delete all candidates in the database")
    public void deleteAllCandidateInDB() {
        LOG.info("Delete all candidates in the database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM SwagScreening.dbo.Candidates");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }

    @Step("Delete the candidate in the database")
    public void deleteCandidateInDB(String email) {
        LOG.info("Delete candidate in the database with email: " + email);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM SwagScreening.dbo.Candidates WHERE Email = '" + email + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }
}
