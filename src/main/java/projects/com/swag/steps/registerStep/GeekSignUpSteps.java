package projects.com.swag.steps.loginStep;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.swag.components.loginComponent.SignUpGeekComponents;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static projects.com.communication.tool.steps.user.UserStep.MIN_PASSWORD_LENGTH;
import static settings.TestConfig.getProperty;

public class GeekSignUpSteps {
    public SignUpGeekComponents components;
    private static final String email = randomAlphabetic(10) + "@i.ii";
    private static final String pass = randomAlphabetic(MIN_PASSWORD_LENGTH);

    public GeekSignUpSteps(WebDriver driver){
        this.components = new SignUpGeekComponents(driver);
    }

    @Step("Open landing page")
    public void openLandingPage() {
        components.open(getProperty("swag.url"));
    }

    @Step("SingUp as a Geek")
    public void signUp(String email,String pass){
        components.getSignUpButton().click();
        components.getCreateGeekButton().click();
        components.getEmailButton().click();
        components.getFirstNameInput().sendKeys("Dima");
        components.getLastNameInput().sendKeys("Test");
        //components.getPhoneCodeMenu();
        components.getPhoneNumberInput().sendKeys("1234567");
        components.getEmailInput().sendKeys(email);
        components.getPasswordInput().sendKeys(pass);
        components.getConfirmPasswordInput().sendKeys(pass);
        components.getAgreeCheckBox().click();
        components.getNextButton().click();
        components.actionSendKeys(components.getSkillsInput(),"php");
        components.getNextButton().click();
        components.getFirstHandle().click();
        components.getNextButton().click();
        components.getFullTime().click();
        components.getMinimumSalaryList().click();
        components.getRandomSalary().click();
        components.getLocationInput().click();
        //components.getNextButton().click();
        components.getFinishButton().click();
    }

    @Step("Verify that user was successfully registered")
    public boolean isUserLogin() {
        return isUserLogin();
    }
}
