package projects.com.swag.screening.steps.problem;

import settings.SQLConnector;
import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.swag.screening.components.problem.TestCaseComponent;

import java.sql.ResultSet;
import java.sql.SQLException;

import static settings.SeleniumListener.LOG;

public class TestCaseStep {

    private TestCaseComponent component;
    static final String PATTERN = "Hello world";

    public TestCaseStep(WebDriver driver){
        this.component = new TestCaseComponent(driver);
    }

    @Step("Create a test case")
    public void createTestCase(){
        component.openTestCasePopUp();
        component.getInputPatternField().sendKeys(PATTERN);
        component.getOutputPatternField().sendKeys(PATTERN);
        component.getSubmitButton().click();
    }

    @Step("Check that test case was created in database and it is linked to the problem")
    public String getProblemIdInTestCaseTableInDB(String problemId){
        LOG.info("Get problem ID in test case table in database with id: " + problemId);
        SQLConnector connector = new SQLConnector();
        String id = null;
        try {
            ResultSet result = connector.
                    executeSelectQuery("SELECT * FROM SwagScreening.dbo.TestCases WHERE ProblemId = '" + problemId + "'");
            result.next();
            id = result.getString("ProblemId");
        } catch (SQLException e) {
            e.printStackTrace();
        }
        connector.closeConnection();
        return id;
    }
}
