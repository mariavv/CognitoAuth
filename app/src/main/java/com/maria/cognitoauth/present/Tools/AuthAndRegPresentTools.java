package com.maria.cognitoauth.present.Tools;

public class AuthAndRegPresentTools {
    public static final int REGISTER_REQUEST = 3;

    private static final int MIN_LENGTH = 1;
    private static final int PASS_MIN_LENGTH = 8;

    public static boolean isPassCorrect(int passLen) {
        return (passLen >= PASS_MIN_LENGTH);
    }

    public static boolean isParamCorrect(int len) {
        return (len >= MIN_LENGTH);
    }

    public static int getTextLength(String s) {
        return s.length();
    }
}
