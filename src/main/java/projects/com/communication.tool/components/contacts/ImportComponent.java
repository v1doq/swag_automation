package projects.com.communication.tool.components.contacts;

import common.BaseComponent;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;

import static org.openqa.selenium.By.className;
import static org.openqa.selenium.By.cssSelector;

public class ImportComponent extends BaseComponent {

    public ImportComponent(WebDriver driver) {
        super(driver);
    }

    public WebElement getUploadButton(){
        return getDriver().findElement(className("file-uploader__hint"));
    }

    public WebElement getFirstDBEntitySelect(){
        return $(cssSelector("td:nth-child(2)"));
    }

    public WebElement getFirstDBFieldSelect(){
        return $(cssSelector("td:nth-child(3)"));
    }

    public WebElement getFirstColumnNameText(){
        return $(cssSelector(".file-mapper-preview"));
    }

    public WebElement getSecondDBEntitySelect(){
        return $(cssSelector("tr:nth-child(2) > td:nth-child(2)"));
    }

    public WebElement getSecondDBFieldSelect(){
        return $(cssSelector("tr:nth-child(2) > td:nth-child(3) "));
    }

    public WebElement getSecondColumnNameText(){
        return $(cssSelector("tr:nth-child(2) > td.file-mapper-preview"));
    }

    public WebElement getThirdDBEntitySelect(){
        return $(cssSelector("tr:nth-child(3) > td:nth-child(2)"));
    }

    public WebElement getThirdDBFieldSelect(){
        return $(cssSelector("tr:nth-child(3) > td:nth-child(3)"));
    }

    public WebElement getThirdColumnNameText(){
        return $(cssSelector("tr:nth-child(3) > td.file-mapper-preview"));
    }

    public WebElement getFourthDBEntitySelect(){
        return $(cssSelector("tr:nth-child(4) > td:nth-child(2)"));
    }

    public WebElement getFourthDBFieldSelect(){
        return $(cssSelector("tr:nth-child(4) > td:nth-child(3)"));
    }

    public WebElement getFourthColumnNameText(){
        return $(cssSelector("tr:nth-child(4) > td.file-mapper-preview"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector(".btn-save"));
    }

    public WebElement getFileNameInHistoryTable(){
        return $(cssSelector("td:nth-child(1)"));
    }
}
