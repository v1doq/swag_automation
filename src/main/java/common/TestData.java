package common;

import java.util.List;

import static java.util.Arrays.asList;
import static org.apache.commons.lang.RandomStringUtils.*;
import static steps.steps.authorization.RegisterStep.*;

public final class TestData {
    public static final List<String> VALID_EMAIL_LIST = asList(
            "email@domain.com",
            "firstname.lastname@domain.com",
            "email@subdomain.domain.com",
            "firstname+lastname@domain.com",
            "1234567890@domain.com",
            "_______@domain.com",
            "email@domain.name",
            "email@domain.co.jp",
            "1@i.ua",
            "firstname-lastname@domain.com"
            );

    public static final List<String> INVALID_EMAIL_LIST = asList(
            "plainaddress",
            "#@%^%#$@#$@#.com",
            "@domain.com",
            "Joe Smith <email@domain.com>",
            "email.domain.com",
            "email@domain@domain.com",
            ".email@domain.com",
            "email.@domain.com",
            "email..email@domain.com",
            "email @ domain .com",
            "email@domain.com (Joe Smith)",
            "email@domain",
            "email@-domain.com",
            "email@111.222.333.44444",
            "email@domain..com",
            " ",
            ""
            );

    public static final List<String> VALID_NAME_LIST = asList(
            randomAlphabetic(MIN_NAME_LENGTH),
            randomAlphabetic(MAX_NAME_LENGTH),
            "Björk",
            "Кириллица"
    );

    public static final List<String> INVALID_NAME_LIST = asList(
            randomAlphabetic(MIN_NAME_LENGTH - 1),
            randomAlphabetic(MAX_NAME_LENGTH + 1),
            randomNumeric(MIN_NAME_LENGTH),
            randomNumeric(MIN_NAME_LENGTH) + " " + randomAlphabetic(MIN_NAME_LENGTH),
            " " + randomAlphabetic(MIN_NAME_LENGTH) + " ",
            "!@#$%",
            " ",
            ""
    );
}
