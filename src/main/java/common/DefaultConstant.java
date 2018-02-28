package common;

import static settings.TestConfig.getProperty;

public class DefaultConstant {

    public static final String USERNAME = getProperty("screening.login");
    public static final String PASSWORD = getProperty("screening.pass");
    public static final String MANAGER_ID = "A1439392-0CBD-42EC-4320-08D5619F8946";
    public static final String CANDIDATE_ACCESS_CODE = "2ZW9Xq7tp0CmS-B2kPCFhA";

    public static final String USERNAME_PASS_TOOLS = getProperty("password.tools.login");
    public static final String PASSWORD_PASS_TOOLS = getProperty("password.tools.pass");
}
