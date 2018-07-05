package projects.com.communication.tool.contacts;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.communication.tool.steps.contacts.ImportStep;
import projects.com.communication.tool.steps.user.LoginStepCT;

import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertTrue;

@Feature("Import")
@Story("Functional tests for import")
public class ImportTest extends BaseTest {

    private ImportStep importStep;

    @BeforeMethod(description = "Authorization", alwaysRun = true)
    public void setUp() {
        importStep = new ImportStep(driver);
        LoginStepCT loginStep = new LoginStepCT(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "sanity import", timeOut = 1200000, description = "Upload file for import")
    public void uploadFileForImport() throws Exception {
        String fullName = randomAlphabetic(10);
        String workEmail = randomAlphabetic(10).toLowerCase() + "@gmail.com";
        String companyName = randomAlphabetic(10);
        String email = randomAlphabetic(10).toLowerCase() + "@outlook.com";

        importStep.updateImportFile(fullName, workEmail, companyName, email);
        importStep.openImportPage();
        importStep.uploadFile();
        assertTrue(importStep.isMappingSuccess(fullName, workEmail, companyName, email));

        importStep.sendFile();
        assertTrue(importStep.isFileNameInHistoryTable());

        assertTrue(importStep.isFullNameSavedToDb(fullName));
        assertTrue(importStep.isWorkEmailSavedToDb(workEmail));
        assertTrue(importStep.isCompanyNameSavedToDb(companyName));
        assertTrue(importStep.isEmailSavedToDb(email));
    }
}
