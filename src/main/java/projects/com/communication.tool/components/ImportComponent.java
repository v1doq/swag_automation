package projects.com.communication.tool.components;

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
        return $(className("file-uploader__hint"));
    }

    public WebElement getColumnNameText(){
        return $(className("file-mapper-title"));
    }

    public WebElement getDBEntitySelect(){
        return $(cssSelector("td:nth-child(2) > div"));
    }

    public WebElement getDBFieldSelect(){
        return $(cssSelector("td:nth-child(3) > div"));
    }

    public WebElement getSubmitButton(){
        return $(cssSelector(".btn-save"));
    }

    public WebElement getFileNameInHistoryTable(){
        return $(cssSelector("td:nth-child(1)"));
    }
}
