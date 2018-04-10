package common;

import io.qameta.allure.Step;
import org.testng.annotations.BeforeSuite;
import projects.com.communication.tool.steps.LoginStepCT;
import settings.LocalStorage;
import settings.SQLConnector;

import static settings.SeleniumListener.LOG;

public class SuiteTestCT extends BaseTest {

    private static String VALUE;
    private static final String KEY = "default_auth_token";
    private LoginStepCT loginStep;
    private LocalStorage storage;

    @BeforeSuite(description = "Get authorization token", alwaysRun = true)
    public void loginAndGetToken() {
        openBrowser();
        storage = new LocalStorage(driver);
        loginStep = new LoginStepCT(driver);
        loginStep.authorization();
        VALUE = storage.getValueFromLocalStorage(KEY);
    }

    @Step("Login with token")
    protected void loginWithToken(){
        storage = new LocalStorage(driver);
        loginStep = new LoginStepCT(driver);
        loginStep.openLandingPage();
        storage.setItemInLocalStorage(KEY, VALUE);
        addCookies();
    }

    protected void cleanDatabase(){
        LOG.info("Try to clean database");
        SQLConnector connector = new SQLConnector();
        connector.executeQuery("USE CommunicationTool DELETE FROM Message; DELETE FROM Conversation; " +
                "DELETE FROM EmailCommunication; DELETE FROM EmailGateway; DELETE FROM RepresentativeCampaign; " +
                "DELETE FROM RepresentativePlaceholder; DELETE FROM Representative; DELETE FROM Campaign; " +
                "DELETE FROM CompanyPlaceholder; DELETE FROM Company;");
        LOG.info("Successfully cleaned");
        connector.closeConnection();
    }
}
