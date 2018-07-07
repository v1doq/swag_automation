package projects.com.communication.tool.steps.contacts;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.contacts.ImportComponent;
import settings.SQLConnector;

import java.io.File;
import java.util.ArrayList;

import static common.ConciseApi.sleep;
import static common.ConciseApi.uploadFileFromModalWindow;
import static common.FileEditor.readFromFile;
import static common.FileEditor.updateFile;
import static org.openqa.selenium.By.cssSelector;
import static projects.com.communication.tool.common.CommStackDB.*;
import static settings.SQLConnector.*;
import static settings.SeleniumListener.LOG;
import static settings.TestConfig.getProperty;

public class ImportStep {

    private ImportComponent component;
    private static String fileName = "import.csv";
    private static String FILE_PATH = System.getProperty("user.dir") + "/src/main/resources/communication.tools/" + fileName;

    public ImportStep(WebDriver driver) {
        this.component = new ImportComponent(driver);
    }

    @Step("Open contacts page")
    public void openImportPage() {
        component.open(getProperty("communication.tool.url") + "contacts/import/");
    }

    @Step("Update import file")
    public void updateImportFile(String firstName, String workEmail, String company, String email) throws Exception {
        String update = firstName + "," + workEmail + "," + company + "," + email;
        updateFile(FILE_PATH, update);
        ArrayList<String> fileContent = readFromFile(FILE_PATH);
        if (!fileContent.get(1).equals(update)) {
            throw new Exception("Values are not equals in the file");
        }
    }

    @Step("Upload file")
    public void uploadFile() {
        component.getUploadButton().click();
        File file = new File(FILE_PATH);
        uploadFileFromModalWindow(file.getAbsolutePath());
    }

    @Step("Verify column mapping")
    public boolean isMappingSuccess(String fullName, String workEmail, String company, String email) {
        return component.getFirstColumnNameText().getText().equals(fullName) &&
                component.getFirstDBEntitySelect().getText().contains("User") &&
                component.getFirstDBFieldSelect().getText().contains("Full Name") &&
                component.getSecondColumnNameText().getText().equals(workEmail) &&
                component.getSecondDBEntitySelect().getText().contains("User") &&
                component.getSecondDBFieldSelect().getText().contains("Work Email") &&
                component.getThirdColumnNameText().getText().equals(company) &&
                component.getThirdDBEntitySelect().getText().contains("Prospect") &&
                component.getThirdDBFieldSelect().getText().contains("Company Name") &&
                component.getFourthColumnNameText().getText().equals(email) &&
                component.getFourthDBEntitySelect().getText().contains("Prospect") &&
                component.getFourthDBFieldSelect().getText().contains("Email");
    }

    @Step("Send file")
    public void sendFile() {
        component.getSubmitButton().click();
    }

    @Step("Verify that file name displayed in history table")
    public boolean isFileNameInHistoryTable() {
        component.getDriver().navigate().refresh();
        component.waitForText(cssSelector("td:nth-child(1)"), fileName);
        return component.getFileNameInHistoryTable().getText().equals(fileName);
    }

    @Step("Is Full Name saved to 'Contact' table in the database")
    public boolean isFullNameSavedToDb(String fullName) {
        SQLConnector connector = new SQLConnector();
        String query = SELECT_FROM + CONTACT_DB + WHERE + "FullName = '" + fullName + "'";
        String value = null;
        while (value == null) {
            value = connector.getValueInDb(query, "FullName");
            LOG.info("Full name does not exist");
            sleep(5000);
        }
        return value.equals(fullName);
    }

    @Step("Is Work Email saved to 'ContactInfo' table in the database")
    public boolean isWorkEmailSavedToDb(String workEmail) {
        SQLConnector connector = new SQLConnector();
        String query = SELECT_FROM + CONTACT_INFO_DB + WHERE + "Value = '" + workEmail + "'";
        String value = connector.getValueInDb(query, "Value");
        return value.equals(workEmail);
    }

    @Step("Is Company Name saved to 'Prospect' table in the database")
    public boolean isCompanyNameSavedToDb(String companyName) {
        SQLConnector connector = new SQLConnector();
        String query = SELECT_FROM + PROSPECT_DB + WHERE + "CompanyName = '" + companyName + "'";
        String value = connector.getValueInDb(query, "CompanyName");
        return value.equals(companyName);
    }

    @Step("Is Email saved to 'ProspectContactInfo' table in the database")
    public boolean isEmailSavedToDb(String email) {
        SQLConnector connector = new SQLConnector();
        String query = SELECT_FROM + PROSPECT_CONTACT_INFO_DB + WHERE + "Value = '" + email + "'";
        String value = connector.getValueInDb(query, "Value");
        return value.equals(email);
    }
}
