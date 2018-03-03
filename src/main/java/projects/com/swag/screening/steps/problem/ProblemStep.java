package projects.com.swag.screening.steps.problem;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.swag.screening.components.problem.ProblemComponent;
import settings.SQLConnector;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static projects.com.swag.screening.steps.problem.TestCaseStep.PATTERN;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class ProblemStep {

    private ProblemComponent component;
    private static final int MIN_DESC_LENGTH = 2;
    public static final int MIN_PROBLEM_NAME_LENGTH = 2;
    private static final int MIN_SOLUTION_TIME = 1;
    private static final int MIN_EXECUTION_TIME = 100;
    private static final int MIN_EXECUTION_MEMORY = 16;

    public ProblemStep(WebDriver driver){
        this.component = new ProblemComponent(driver);
    }

    @Step("Open problem's page")
    public void openProblemPage() {
        component.open(getProperty("screening.url") + "problems/");
    }

    @Step("Create a problem")
    public void createProblem(String name){
        component.openProblemPopUp();
        component.getNameInput().sendKeys(name);
        component.getDescriptionInput().sendKeys(randomAlphabetic(MIN_DESC_LENGTH));
        component.getLanguageCheckbox().click();
        component.getSolutionTimeInput().sendKeys(String.valueOf(MIN_SOLUTION_TIME));
        component.getExecutionTimeInput().sendKeys(String.valueOf(MIN_EXECUTION_TIME));
        component.getExecutionMemoryInput().sendKeys(String.valueOf(MIN_EXECUTION_MEMORY));
        component.getSubmitButton().click();
    }

    public void findProblemInList(String problemName){
        component.getSearchField().sendKeys(problemName);
        component.waitForSearchResult(problemName);
    }

    @Step("Create a problem in the database")
    public void createProblemInDB(String problemName){
        LOG.info("Create a problem in the database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @problemId uniqueidentifier\n" +
                "SET @problemId = NEWID() \n" +
                "INSERT INTO SwagScreening.dbo.Problems\n" +
                "(Id, Description, ExecutionMemoryLimit, ExecutionTimeLimit, Name, [Time])\n" +
                "VALUES(@problemId, 'test', 100, 100, '" + problemName + "', '01:40:00.000');");
        connector.closeConnection();
    }

    @Step("Check that problem was created in the database")
    public String getProblemNameInDB(String problemName){
        LOG.info("Get the problem in the database with name: " + problemName);
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM SwagScreening.dbo.Problems WHERE Name = '" + problemName + "'";
        return connector.getStringValueInDB(query, "Name");
    }

    public String getProblemIdInDB(String problemName){
        LOG.info("Get the problem's ID in the database with name: " + problemName);
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM SwagScreening.dbo.Problems WHERE Name = '" + problemName + "'";
        return connector.getStringValueInDB(query, "Id");
    }

    @Step("Create a problem with test case in the database")
    public void createProblemWithTestCaseInDB(String problemName){
        LOG.info("Create a problem with test case in the database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DECLARE @problemId uniqueidentifier\n" +
                "SET @problemId = NEWID() \n" +
                "INSERT INTO SwagScreening.dbo.Problems\n" +
                "(Id, Description, ExecutionMemoryLimit, ExecutionTimeLimit, Name, [Time])\n" +
                "VALUES(@problemId, 'test', 100, 100, '" + problemName + "', '01:40:00.000');\n" +
                "DECLARE @testCaseId uniqueidentifier\n" +
                "SET @testCaseId = NEWID() \n" +
                "INSERT INTO SwagScreening.dbo.TestCases\n" +
                "(Id, InputPattern, OutputPattern, ProblemId)\n" +
                "VALUES(@testCaseId, '" + PATTERN + "', '" + PATTERN + "', @problemId);\n" +
                "DECLARE @problemLanguagesId uniqueidentifier\n" +
                "SET @problemLanguagesId = NEWID() \n" +
                "INSERT INTO SwagScreening.dbo.ProblemLanguages\n" +
                "(Id, LanguageId, ProblemId)\n" +
                "VALUES(@problemLanguagesId, 'CBE84B9C-7DDA-4A2A-6F06-08D5619AAC8A', @problemId);\n");
        connector.closeConnection();
    }

    @Step("Delete all problems in the database")
    public void deleteAllProblemInDB() {
        LOG.info("Delete all problems in the database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM SwagScreening.dbo.Problems");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }

    @Step("Delete the problems in the database")
    public void deleteProblemInDB(String problemName) {
        LOG.info("Delete problems in the database with name: " + problemName);
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("DELETE FROM SwagScreening.dbo.Problems WHERE Name = '" + problemName + "'");
        connector.closeConnection();
        LOG.info("Successfully deleted");
    }
}
