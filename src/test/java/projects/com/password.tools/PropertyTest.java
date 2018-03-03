package projects.com.password.tools;

import common.BaseTest;
import io.qameta.allure.Feature;
import io.qameta.allure.Severity;
import io.qameta.allure.SeverityLevel;
import io.qameta.allure.Story;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;
import projects.com.password.tools.steps.LoginStep;
import projects.com.password.tools.steps.PropertyStep;
import projects.com.password.tools.steps.TableStep;

import static org.apache.commons.lang.RandomStringUtils.randomAlphabetic;
import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertFalse;
import static projects.com.password.tools.steps.PropertyStep.MAX_PROPERTY_LENGTH;

@Feature("Property")
@Story("Functional tests for CRUD property")
public class PropertyTest extends BaseTest {

    private TableStep tableStep;
    private PropertyStep propertyStep;

    @BeforeMethod(description = "Precondition", alwaysRun = true)
    public void setUp() {
        tableStep = new TableStep(driver);
        propertyStep = new PropertyStep(driver);
        LoginStep loginStep = new LoginStep(driver);
        loginStep.authorization();
    }

    @Severity(SeverityLevel.CRITICAL)
    @Test(groups = "smoke test", description = "Create, update and delete a property")
    public void createUpdateDeleteProperty() {
        String propertyName = randomAlphabetic(MAX_PROPERTY_LENGTH / 3);
        propertyStep.openPropertyPage();
        propertyStep.createProperty(propertyName);
        String nameInDB = propertyStep.getPropertyNameInDB(propertyName);
        assertEquals(propertyName, nameInDB);

        tableStep.searchInTable(propertyName);
        String newPropertyName = randomAlphabetic(MAX_PROPERTY_LENGTH / 3);
        propertyStep.updateProperty(newPropertyName);
        String newNameInDB = propertyStep.getPropertyNameInDB(newPropertyName);
        assertEquals(newPropertyName, newNameInDB);

        tableStep.searchInTable(newPropertyName);
        propertyStep.deleteProperty();
        assertFalse(tableStep.isValueDisplayInTable(newPropertyName));
    }
}
