package projects.com.password.tools.steps;

import io.qameta.allure.Step;
import org.openqa.selenium.WebDriver;
import projects.com.password.tools.components.ProfileComponent;

import static org.openqa.selenium.By.name;
import static org.openqa.selenium.support.ui.ExpectedConditions.invisibilityOfElementLocated;
import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOf;

public class ProfileStep {

    private ProfileComponent component;
    private CategoryStep categoryStep;

    public ProfileStep(WebDriver driver) {
        this.component = new ProfileComponent(driver);
        this.categoryStep = new CategoryStep(driver);
    }

    @Step("Open profile pop up")
    public void goToProfilePopUp() {
        categoryStep.openCategoryPage(); //there is no change pass locator
        component.getEditProfileButton().click();
        component.assertThat(visibilityOf(component.getUsernameInput()));
    }

    @Step("Open profile pop up")
    public void editUsernameAndPass(String username, String userPass, String newPass) {
        component.clearAndSendKeys(component.getUsernameInput(), username);
        component.getChangePassButton().click();
        component.getYourPassInput().sendKeys(userPass);
        component.getNewPassInput().sendKeys(newPass);
        component.getOkButton().click();
        component.assertThat(invisibilityOfElementLocated(name("change-password-password")));
        component.getSubmitButton().click();
        component.assertThat(invisibilityOfElementLocated(name("edit-profile-username")));
    }
}
