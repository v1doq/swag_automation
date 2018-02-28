package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.EmployeeComponent;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.apache.commons.lang.RandomStringUtils.randomAlphanumeric;

public class EmployeeStep {

    private EmployeeComponent component;
    private static final int MIN_NAME_LENGTH = 1;
    public static final int MAX_NAME_LENGTH = 50;
    private static final int MIN_TITLE_LENGTH = 1;

    public EmployeeStep(WebDriver driver) {
        this.component = new EmployeeComponent(driver);
    }

    @Step("Create new user")
    public void createUser(String username, String pass){
        String email = randomAlphabetic(10) + "@" + randomAlphanumeric(2) + "." + randomAlphabetic(2);
        component.getCreateButton().click();
        component.getNameInput().sendKeys(randomAlphabetic(MIN_NAME_LENGTH));
        component.getUsernameInput().sendKeys(username);
        component.getTitleInput().sendKeys(randomAlphabetic(MIN_TITLE_LENGTH));
        component.getEmailInput().sendKeys(email);
        component.getPasswordInput().sendKeys(pass);
        component.getSubmitButton().click();
    }
}
