package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.testng.annotations.DataProvider;
import projects.com.communication.tool.components.campaign.RepresentativeComponent;
import settings.SQLConnector;

import static common.ConciseApi.sleep;
import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.openqa.selenium.By.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static settings.SeleniumListener.LOG;

public class RepresentativeStep {

    private RepresentativeComponent component;
    public static final byte MIN_FROM_NAME_LENGTH = 1;
    public static final String GATEWAY_OUTLOOK_EMAIL = "communication.auto@outlook.com";
    private static final String GATEWAY_OUTLOOK_PASS = "Passcommunication1";
    public static final String GATEWAY_GMAIL_EMAIL = "communication.tool.test@gmail.com";
    private static final String GATEWAY_GMAIL_PASS = "passcommunication";

    public RepresentativeStep(WebDriver driver) {
        this.component = new RepresentativeComponent(driver);
    }

    @Step("Create representative")
    public void createRepresentative(String email, String fromName) {
        openRepsFormIfNotDisplayed();
        component.getFromNameInput().sendKeys(fromName);
        if (email.equals(GATEWAY_OUTLOOK_EMAIL)) {
            component.getFromEmailInput().sendKeys(email);
            component.getLoginInput().sendKeys(email);
            component.getPasswordInput().sendKeys(GATEWAY_OUTLOOK_PASS);
        } else {
            component.getFromEmailInput().sendKeys(email);
            component.getLoginInput().sendKeys(email);
            component.getPasswordInput().sendKeys(GATEWAY_GMAIL_PASS);
            component.jsClearAndSendKeys(component.getSmtpHostInput(), "smtp.gmail.com:587");
            component.jsClearAndSendKeys(component.getImapHostInput(), "imap.gmail.com:993");
        }
        saveRepresentative();
    }

    @Step("Create representative with placeholder")
    public void createRepsWithPlaceholder(String email, String fromName, String key, String value) {
        createPlaceholder(key, value);
        createRepresentative(email, fromName);
    }

    @Step("Update representative")
    public void updateReps(String fromName) {
        component.getEditRepsButton().click();
        component.clickToElementInListByText("Edit", component.getRepsActions());
        component.clearAndSendKeys(component.getFromNameInput(), fromName);
        saveRepresentative();
    }

    @Step("Delete representative")
    public void deleteReps() {
        component.getEditRepsButton().click();
        component.clickToElementInListByText("Delete", component.getRepsActions());
        component.getDeleteRepsButton().click();
        component.assertThat(invisibilityOf(component.getDeleteRepsButton()));
    }

    @Step("Save representative")
    public void saveRepresentative() {
        component.getRepsTitle().click();
        component.getSubmitButton().click();
        while (component.isElementPresent(name("login"))) {
            sleep(2000);
            if (component.isTextDisplayed("settings error", className("error-message"))) {
                break;
            }
        }
        if (!component.isElementPresent(name("login"))) {
            component.assertThat(visibilityOf(component.getFromNameValue()));
        }
    }

    @Step("Is representative was created")
    public boolean isFromNameDisplayedInRepsCards(String fromName) {
        boolean isDisplayed;
        if (component.isTextDisplayed("settings error", className("error-message"))) {
            return false;
        } else {
            sleep(2000);
            isDisplayed = component.isTextDisplayed(fromName, className("card__textfield"));
        }
        return isDisplayed;
    }

    @Step("Fill representative's fields")
    public void fillRepsFields(String email, String fromName) {
        openRepsFormIfNotDisplayed();
        component.getFromNameInput().sendKeys(fromName);
        component.getFromEmailInput().sendKeys(email);
    }

    @Step("Fill gateway's fields")
    public void fillGatewayFields(String email, String pass, String smtp, String imap) {
        component.getLoginInput().sendKeys(email);
        component.getPasswordInput().sendKeys(pass);
        component.jsClearAndSendKeys(component.getSmtpHostInput(), smtp);
        component.jsClearAndSendKeys(component.getImapHostInput(), imap);
    }

    @Step("Create placeholder")
    private void createPlaceholder(String key, String value) {
        openRepsFormIfNotDisplayed();
        component.getNewPlaceholderButton().click();
        component.getPlaceholderKeyInput().sendKeys(key, Keys.ENTER);
        component.getPlaceholderValueInput().sendKeys(value);
    }

    @Step("Is placeholder displayed in list")
    public boolean isPlaceholderDisplayedInList(String key) {
        openRepsFormIfNotDisplayed();
        component.getNewPlaceholderButton().click();
        component.getPlaceholderKeyInput().click();
        WebElement element = component.getElementInListByText(key, className("list__tile__title"));
        return element.getText().equals(key);
    }

    @Step("Is adding representative form displayed")
    public boolean isEmptyRepsFormDisplayed() {
        component.assertThat(visibilityOf(component.getFromNameInput()));
        return component.isElementPresent(cssSelector(".btn-save.btn-medium"));
    }

    @Step("Is from name in the database equals to from name on front")
    public boolean isFromNameEqualsDbValue(String fromName) {
        LOG.info("Get reps value in the database with fromName: " + fromName);
        SQLConnector connector = new SQLConnector();
        String query = "SELECT * FROM CommunicationTool.dbo.RepresentativePlaceholder WHERE Value = '" + fromName + "'";
        String valueInDB = connector.getStringValueInDB(query, "Value");
        return valueInDB.equals(fromName);
    }

    private void openRepsFormIfNotDisplayed() {
        if (component.isElementPresent(cssSelector(".gateways__add-new > button"))) {
            component.jsClick(component.getAddButton());
            component.assertThat(elementToBeClickable(component.getNewPlaceholderButton()));
        }
    }

    @DataProvider(name = "Valid")
    public static Object[][] validCredential() {
        return new Object[][]{
                {"smtp.outlook.com:25", "imap-mail.outlook.com:993", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
                {"smtp.outlook.com:587", "imap-mail.outlook.com:993", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
                {"smtp-mail.outlook.com:587", "outlook.office365.com", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
                {"smtp.office365.com:587", "imap.outlook.com:993", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
                {"smtp.gmail.com:25", "imap.gmail.com:993", GATEWAY_GMAIL_EMAIL, GATEWAY_GMAIL_PASS},
                {"smtp.gmail.com:465", "imap.gmail.com:993", GATEWAY_GMAIL_EMAIL, GATEWAY_GMAIL_PASS},
                {"smtp.gmail.com:587", "imap.gmail.com:993", GATEWAY_GMAIL_EMAIL, GATEWAY_GMAIL_PASS}
        };
    }

    @DataProvider(name = "Invalid")
    public static Object[][] invalidCredential() {
        return new Object[][]{
                {"smtp.outlook.com:587", "imap.outlook.com:143", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
                {"smtp.gmail.com:587", "imap.gmail.com:143", GATEWAY_GMAIL_EMAIL, GATEWAY_GMAIL_PASS},
                {"smtp.outlook.com:587", "imap.outlook.com:993", GATEWAY_OUTLOOK_EMAIL, randomAlphabetic(1)},
                {"smtp.outlook.com:587", "imap.outlook.com:993", randomAlphabetic(1), GATEWAY_OUTLOOK_PASS},
                {"pop3.outlook.com:587", "imap.outlook.com:993", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
        };
    }
}
