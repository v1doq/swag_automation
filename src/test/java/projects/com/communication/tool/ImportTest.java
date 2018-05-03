package projects.com.communication.tool;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Ignore;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.ImportStep;
import projects.com.communication.tool.steps.LoginStepCT;

import static org.testng.Assert.assertTrue;

@Feature("Import")
@Story("Functional tests for file import")
public class ImportTest extends BaseTest {

    private ImportStep importStep;

    @BeforeMethod(description = "Authorization", alwaysRun = true)
    public void setUp() {
        importStep = new ImportStep(driver);
        LoginStepCT loginStep = new LoginStepCT(driver);
        loginStep.authorization();
    }

    @Ignore
    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Upload valid file")
    public void uploadFile() {
        importStep.openImportPage();
        importStep.uploadFile();
        assertTrue(importStep.isMappingSuccess());

        importStep.sendFile();
        assertTrue(importStep.isFileNameInHistoryTable());
    }
}
