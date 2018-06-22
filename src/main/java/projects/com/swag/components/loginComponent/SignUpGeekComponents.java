package projects.com.swag.components.loginComponent;

import org.apache.commons.lang3.RandomStringUtils;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.*;

public class SignUpGeekComponents extends SignUpEmpComponents {

    public SignUpGeekComponents(WebDriver driver) {
        super(driver);
    }

    public WebElement getCreateGeekButton() {
        return $(cssSelector("[href='/GeekSignUp']"));
    }

    public WebElement getSkillsInput() {
        return $(className("Select-placeholder"));
    }

    public WebElement getTheNextButton() {
        return $(className("button.emph"));
    }

    public WebElement getAddSkillButton() {
        return $(className("line-control-content.emph"));
    }

    public WebElement getSkillList() {
        return $(className("skill-list"));
    }

    public WebElement getBackButton() {
        return $(className("back"));
    }

    public WebElement getHandleInput() {
        return $(name("Handle"));
    }

    public WebElement getFirstHandle(){
        return $(cssSelector("div.content > div > div > span:nth-child(1)"));
    }

    public WebElement getRefreshButton() {
        return $(cssSelector("div.content > div > span"));
    }

    //need to check locator
    public WebElement getHandlesList() {
        return $(cssSelector("#geek-handle > div > div.content > div > div"));
    }

    public WebElement getFullTime() {
        return $(cssSelector("div:nth-child(1) > div:nth-child(2) > div > div > div > i"));
    }

    public WebElement getContract() {
        return $(cssSelector("div:nth-child(1) > div:nth-child(3) > div > div > div > i"));
    }

    public WebElement getOnDemandProjects() {
        return $(cssSelector("div:nth-child(1) > div:nth-child(4) > div > div > div > i"));
    }

    public WebElement getLocationInput() {
        return $(className("Select-placeholder"));
    }

    public WebElement getCurrentLocation(){
        return $(cssSelector("div.inner-content"));
    }

    public WebElement getRelocationButton() {
        return $(cssSelector("div:nth-child(2) > div:nth-child(1) > div > div > div > i"));
    }

    public WebElement getRemoteWorkButton() {
        return $(cssSelector("div:nth-child(2) > div:nth-child(2) > div > div > div > i"));
    }

    public WebElement getMinimumSalaryList() {
        return $(name("Salary"));
    }

    public WebElement getRandomSalary(){
        return $(cssSelector("option:nth-child(1"+RandomStringUtils.random(1,"0123456789") +")"));
    }

    public WebElement getMinnimumHourlyRate() {
        return $(name("HourlyRate"));
    }

    public WebElement getRandomHourlyRate() {
        return $(cssSelector("option:nth-child(1"+RandomStringUtils.random(1,"0123456789") +")"));
    }

    public WebElement getDoneButton() {
        return $(partialLinkText("Done"));
    }
}
