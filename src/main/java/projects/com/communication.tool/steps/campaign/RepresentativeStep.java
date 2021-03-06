package projects.com.communication.tool.steps.campaign;

import io.qameta.allure.Step;
import org.openqa.selenium.*;
import org.testng.annotations.DataProvider;
import projects.com.communication.tool.components.campaign.RepresentativeComponent;
import settings.SQLConnector;

import java.util.List;

import static common.ConciseApi.sleep;
import static java.util.Arrays.asList;
import static org.apache.commons.lang3.RandomStringUtils.randomAlphabetic;
import static org.openqa.selenium.By.*;
import static org.openqa.selenium.support.ui.ExpectedConditions.*;
import static projects.com.communication.tool.common.CommStackDB.*;
import static settings.SQLConnector.*;

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
            component.clearAndSendKeys(component.getSmtpHostInput(), "smtp.gmail.com:587");
            component.clearAndSendKeys(component.getImapHostInput(), "imap.gmail.com:993");
        }
        saveRepresentative(fromName);
    }

    @Step("Create representative with placeholder")
    public void createRepsWithPlaceholder(String email, String fromName, String key, String value) {
        createPlaceholder(key, value);
        createRepresentative(email, fromName);
    }

    @Step("Update representative")
    public void updateRepresentative(String fromName) {
        component.getEditRepsButton().click();
        component.getElementInListByText("Edit", component.getRepsActions()).click();
        component.clearAndSendKeys(component.getFromNameInput(), fromName);
        saveRepresentative(fromName);
    }

    @Step("Delete representative")
    public void deleteRepresentative() {
        component.getEditRepsButton().click();
        component.getElementInListByText("Delete", component.getRepsActions()).click();
        component.getDeleteRepsButton().click();
        component.assertThat(invisibilityOf(component.getDeleteRepsButton()));
    }

    @Step("Save representative")
    public void saveRepresentative(String fromName) {
        clickSaveButton();
        while (component.isElementPresent(name("login"))) {
            sleep(2000);
            if (component.isTextDisplayed("settings error", className("error-message"))) {
                break;
            }
        }
        if (!component.isElementPresent(name("login"))) {
            component.waitForTextInList(component.getFromNameValue(), fromName);
        }
    }

    @Step("Click save button")
    public void clickSaveButton() {
        component.getRepsTitle().click();
        component.getSubmitButton().click();
    }

    @Step("Is representative was created")
    public boolean isFromNameDisplayedInRepsCards(String fromName) {
        if (component.isElementPresent(name("login"))){
            return false;
        }
        return component.isTextDisplayed(fromName, component.getFromNameValue());
    }

    @Step("Fill representative's fields")
    public void fillRepsFields(String email, String fromName) {
        openRepsFormIfNotDisplayed();
        component.getFromNameInput().sendKeys(fromName);
        component.getFromEmailInput().sendKeys(email);
    }

    @Step("Fill gateway's fields")
    public void fillGatewayFields(String email, String pass, String smtp, String imap) {
        openRepsFormIfNotDisplayed();
        component.getLoginInput().sendKeys(email);
        component.getPasswordInput().sendKeys(pass);
        component.clearAndSendKeys(component.getSmtpHostInput(), smtp);
        component.clearAndSendKeys(component.getImapHostInput(), imap);
    }

    @Step("Create placeholder")
    public void createPlaceholder(String key, String value) {
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
        WebElement element = component.getElementInListByText(key, className("v-list__tile__title"));
        return element.getText().equals(key);
    }

    @Step("Is adding representative form displayed")
    public boolean isEmptyRepsFormDisplayed() {
        try {
            component.assertThat(visibilityOf(component.getFromNameInput()));
        } catch (TimeoutException e) {
            return false;
        }
        return true;
    }

    @Step("Is error validation messages are displayed")
    public boolean isValidationMessagesDisplayed(List<String> errors) {
        boolean isDisplayed = false;
        for (String error : errors) {
            isDisplayed = component.isTextDisplayed(error, tagName("div"));
            if (!isDisplayed) {
                break;
            }
        }
        return isDisplayed;
    }

    @Step("Is from name in the database equals to from name on front")
    public boolean isFromNameEqualsDbValue(String fromName) {
        SQLConnector connector = new SQLConnector();
        String query = SELECT_FROM + REP_PLACEHOLDER_DB + WHERE + "Value = '" + fromName + "'";
        String valueInDB = connector.getValueInDb(query, "Value");
        return valueInDB.equals(fromName);
    }

    @DataProvider(name = "Valid")
    public static Object[][] validCredential() {
        return new Object[][]{
                {"smtp.outlook.com:587", "imap-mail.outlook.com:993", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
                {"smtp.office365.com:587", "imap.outlook.com:993", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
                {"smtp.gmail.com:465", "imap.gmail.com:993", GATEWAY_GMAIL_EMAIL, GATEWAY_GMAIL_PASS},
                {"smtp.gmail.com:587", "imap.gmail.com:993", GATEWAY_GMAIL_EMAIL, GATEWAY_GMAIL_PASS}
        };
    }

    @DataProvider(name = "Invalid")
    public static Object[][] invalidCredential() {
        return new Object[][]{
                {"smtp.outlook.com:587", "imap.outlook.com:993", randomAlphabetic(1), randomAlphabetic(1)},
                {"smtp.outlook.com:587", "imap.outlook.com:143", GATEWAY_OUTLOOK_EMAIL, GATEWAY_OUTLOOK_PASS},
                {"smtp.gmail.com:587", "imap.gmail.com:143", GATEWAY_GMAIL_EMAIL, GATEWAY_GMAIL_PASS},
        };
    }

    public static final List<String> REQUIRED_FIELDS_ERRORS = asList(
            "The value field is required.",
            "The login field is required.",
            "The placeholder field is required.",
            "The password field is required.",
            "The SMTP host field is required.",
            "The IMAP host field is required."
    );

    private void openRepsFormIfNotDisplayed() {
        if (component.isElementPresent(cssSelector(".gateways__add-new > button"))) {
            component.jsClick(component.getAddButton());
            component.assertThat(elementToBeClickable(component.getNewPlaceholderButton()));
        }
    }
}
