package common;

import static settings.TestConfig.getProperty;

public class DefaultConstant {

    //screening
    public static final String SCREENING_USERNAME = getProperty("screening.login");
    public static final String SCREENING_PASSWORD = getProperty("screening.pass");
    public static final String MANAGER_ID = "A1439392-0CBD-42EC-4320-08D5619F8946";
    public static final String CANDIDATE_ACCESS_CODE = "2ZW9Xq7tp0CmS-B2kPCFhA";

    //password tools
    public static final String USERNAME_PASS_TOOLS = getProperty("password.tools.login");
    public static final String PASSWORD_PASS_TOOLS = getProperty("password.tools.pass");
    public static final String VALID_PASSWORD = "frDPgoZ#Y5"; //hash below
    public static final String PASSWORD_HASH = "AQAAAAEAACcQAAAAELZ5azJTuJbzjx7ribdOqzPcgG3jEfqT2vtwV5hEAzgSnd723bMyBN6D1qnwQQV8Zw==";
    public static final int USER_ROLE = 0;
    public static final int ADMIN_ROLE = 1;
}
