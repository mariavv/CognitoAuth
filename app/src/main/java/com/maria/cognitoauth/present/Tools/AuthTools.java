package com.maria.cognitoauth.present.Tools;

public class AuthTools {
    private static final int MIN_LENGTH = 1;
    private static final int PASS_MIN_LENGTH = 8;

    public static boolean isPassCorrect(final int passLen) {
        return (passLen >= PASS_MIN_LENGTH);
    }

    public static boolean isParamCorrect(final int len) {
        return (len >= MIN_LENGTH);
    }
}
