package settings;

import org.apache.log4j.Logger;
import org.testng.*;

public class TestNGListener implements ITestListener, ISuiteListener, IClassListener {

    private Logger LOG = Logger.getLogger(TestNGListener.class);

    public void onStart(ITestContext context) {

    }

    public void onFinish(ITestContext context) {

    }

    public void onStart(ISuite suite) {
        LOG.info("STARTING SUITE: " + suite.getName());
    }

    public void onFinish(ISuite suite) {
        LOG.info("FINISHES SUITE: " + suite.getName());
    }

    public void onBeforeClass(ITestClass testClass) {
        LOG.info("STARTING CLASS: " + testClass.getRealClass().getSimpleName());
    }

    public void onAfterClass(ITestClass testClass) {
        LOG.info("FINISHES CLASS: " + testClass.getRealClass().getSimpleName() + "\n");
    }

    public void onTestStart(ITestResult result) {
        LOG.info("STARTING TEST: " + getClassAndMethodName(result));
    }

    public void onTestSuccess(ITestResult result) {
        LOG.info("FINISHES TEST: " + getClassAndMethodName(result));
        printTestResults(result);
    }

    public void onTestFailure(ITestResult result) {
        printTestResults(result);
        LOG.error(result.getThrowable() + "\n");
    }

    public void onTestSkipped(ITestResult result) {
        printTestResults(result);
    }

    public void onTestFailedButWithinSuccessPercentage(ITestResult result) {

    }

    private String getClassAndMethodName(ITestResult result) {
        return result.getTestClass().getRealClass().getSimpleName() + "."
                + result.getMethod().getConstructorOrMethod().getName();
    }

    private void printTestResults(ITestResult result) {
        String status = null;
        switch (result.getStatus()) {
            case ITestResult.SUCCESS:
                status = "PASS" + "\n";
                break;
            case ITestResult.FAILURE:
                status = "FAILED";
                break;
            case ITestResult.SKIP:
                status = "SKIPPED" + "\n";
        }
        LOG.info("TEST STATUS: " + status);
    }
}
