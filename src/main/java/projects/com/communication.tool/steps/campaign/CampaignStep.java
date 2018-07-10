package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.TimeoutException;
import org.openqa.selenium.WebDriver;
import projects.com.communication.tool.components.campaign.CampaignComponent;
import settings.SQLConnector;

import java.util.List;
import java.util.UUID;

import static java.util.Arrays.asList;
import static java.util.UUID.randomUUID;
import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.tagName;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static projects.com.communication.tool.common.CommStackDB.*;
import static settings.SQLConnector.*;
import static settings.TestConfig.getProperty;

public class CampaignStep {

    private CampaignComponent component;
    public static final byte MIN_COMPANY_NAME_LENGTH = 5;
    public static final byte MIN_CAMPAIGN_NAME_LENGTH = 5;
    public static final byte MIN_CAMPAIGN_DESC_LENGTH = 1;
    public static final String DUPLICATE_CAMPAIGN_ERROR = "Campaign with this name already exists";
    public static final String NO_REPRESENTATIVE_FOUND = "No active representatives found";
    public static final String NO_CONTACT_FOUND = "No contact filters found";
    public static final String NO_TEMPLATE_FOUND = "Flow template cannot be empty";

    public CampaignStep(WebDriver driver) {
        this.component = new CampaignComponent(driver);
    }

    @Step("Open campaign page")
    public void openCampaignPage() {
        component.open(getProperty("communication.tool.url") + "campaigns/");
    }

    @Step("Create campaign")
    public void createCampaign(String campaignName, String companyName) {
        component.getCreateCampaignButton().click();
        component.getCompanyNameInput().sendKeys(companyName, Keys.ENTER);
        component.getCampaignNameInput().sendKeys(campaignName);
        component.getModalTitle().click();
        component.actionClick(component.getSubmitButton());
    }

    @Step("Select campaign in list")
    public void selectCampaignInList(String campaignName) {
        searchInCampaignList(campaignName);
        component.assertThat(elementToBeClickable(component.getCampaignInList()));
        component.getElementInListByText(campaignName, component.getCampaignInList()).click();
        try {
            component.waitForText(component.getCampaignNameInPreviewLocator(), campaignName);
        } catch (TimeoutException e) {
            while (!component.getCampaignNameInPreview().getText().equals(campaignName)) {
                component.getDriver().navigate().refresh();
                selectCampaignInList(campaignName);
                if (component.getCampaignNameInPreview().getText().equals(campaignName)) {
                    break;
                }
            }
        }
    }

    @Step("Update campaign's name")
    public void updateCampaignName(String campaignName) {
        component.getEditCampaignNameButton().click();
        component.clearAndSendKeys(component.getUpdateCampaignNameInput(), campaignName);
        component.getUpdateCampaignNameButton().click();
        component.waitForText(component.getCampaignNameInPreviewLocator(), campaignName);
    }

    @Step("Update campaign's desc")
    public void updateCampaignDesc(String campaignDesc) {
        component.getEditCampaignDescButton().click();
        component.clearAndSendKeys(component.getUpdateCampaignDescInput(), campaignDesc);
        component.getUpdateCampaignDescButton().click();
        if (!component.getCampaignDescInPreview().getText().equals(campaignDesc)) {
            component.waitForText(component.getCampaignDescInPreviewLocator(), campaignDesc);
        }
    }

    @Step("Delete campaign")
    public void deleteCampaign() {
        component.getDeleteCampaignPreview().click();
        component.getDeleteCampaignButton().click();
        component.getConfirmDeletionButton().click();
        component.assertThat(invisibilityOf(component.getConfirmDeletionButton()));
    }

    public String getCampaignNameInPreview() {
        return component.getCampaignNameInPreview().getText();
    }

    public String getCampaignDescInPreview() {
        return component.getCampaignDescInPreview().getText();
    }

    @Step("Activate communication")
    public void activateCommunication() {
        component.scrollUp(component.getCommunicationButton());
        component.getCommunicationButton().click();
    }

    @Step("Verify that communication successfully started")
    public boolean isCommunicationStarted() {
        try {
            component.waitForText(component.getSendingStatus(), "In progress");
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    @Step("Stop communication")
    public void stopCommunication() {
        component.scrollUp(component.getCommunicationButton());
        component.getCommunicationButton().click();
        component.waitForText(component.getSendingStatus(), "Paused");
    }

    @Step("Verify that company and campaign are displayed in list")
    public boolean isCompanyAndCampaignInList(String companyName, String campaignName) {
        component.assertThat(attributeToBe(className("modal-mask"), "style", "display: none;"));
        searchInCampaignList(companyName);
        try {
            component.waitForText(component.getCompanyInList(), companyName);
        } catch (TimeoutException e) {
            return false;
        }
        return component.isTextDisplayed(campaignName, component.getCampaignInList());
    }

    @Step("Is server error message displayed")
    public boolean isServerErrorDisplayed(String error) {
        try {
            component.waitForText(component.getServerError(), error);
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    @Step("Is error validation messages are displayed")
    public boolean isValidationMessagesDisplayed() {
        boolean isDisplayed = false;
        for (String error : REQUIRED_FIELDS_ERRORS) {
            isDisplayed = component.isTextDisplayed(error, tagName("div"));
            if (!isDisplayed) {
                break;
            }
        }
        return isDisplayed;
    }

    private static final List<String> REQUIRED_FIELDS_ERRORS = asList(
            "The name field is required.",
            "The company field is required."
    );

    private void searchInCampaignList(String value) {
        component.clearAndSendKeys(component.getSearchInput(), value);
    }

    @Step("Verify that campaign is assign to company")
    public String getCampaignNameByCompanyInDb(String campaignName, String companyName) {
        SQLConnector connector = new SQLConnector();
        String companyId = connector.getValueInDb(SELECT_FROM + COMPANY_DB + WHERE + "Name = '" + companyName + "'", "Id");
        String name = SELECT_FROM + CAMPAIGN_DB + WHERE + "CompanyId='" + companyId + "' AND Name='" + campaignName + "'";
        return connector.getValueInDb(name, "Name");
    }

    @Step("Create campaign in the database")
    public void createCampaignInDB(String campaignName, String companyName) {
        SQLConnector connector = new SQLConnector();
        UUID companyId = randomUUID();
        UUID campaignId = randomUUID();
        String query = INSERT_INTO + COMPANY_DB + "(Id, Name)" + VALUES + "('" + companyId + "', '" + companyName + "');" +
                INSERT_INTO + CAMPAIGN_DB + VALUES + "('" + campaignId + "', '" + companyId + "', NULL, " +
                "'" + campaignName + "', '23:00:00.000', 30, '06:00:00.000', " + "'FLE Standard Time', 254, " +
                "{ts '2018-06-13 09:04:06.854'}, NULL, 1, 0);";
        connector.executeQuery(query);
    }
}
