package projects.com.communication.tool.steps.contacts;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.contacts.ImportComponent;

import static common.ConciseApi.getFirstSelectedOption;
import static org.openqa.selenium.By.cssSelector;
import static settings.TestConfig.getProperty;

public class ImportStep {

    private ImportComponent component;
    private String fileName = "sample.csv";
    private final String VALID_FILE = getProperty("user.dir") + "/src/main/resources/communication.tools/" + fileName;

    public ImportStep(WebDriver driver) {
        this.component = new ImportComponent(driver);
    }

    @Step("Open contacts page")
    public void openImportPage() {
        component.open(getProperty("communication.tool.url") + "contacts/import/");
    }

    @Step("Upload file")
    public void uploadFile() {
        component.getUploadButton().sendKeys(VALID_FILE);
    }

    @Step("Verify column mapping")
    public boolean isMappingSuccess() {
        return component.getColumnNameText().getText().equals("FirstName") &&
                getFirstSelectedOption(component.getDBEntitySelect()).equals("User") &&
                getFirstSelectedOption(component.getDBFieldSelect()).equals("First Name");
    }

    @Step("Send file")
    public void sendFile() {
        component.getSubmitButton().click();
    }

    @Step("Verify that file name displayed in history table")
    public boolean isFileNameInHistoryTable() {
        component.waitForText(cssSelector("td:nth-child(1)"), fileName);
        return component.getFileNameInHistoryTable().getText().equals(fileName);
    }
}
