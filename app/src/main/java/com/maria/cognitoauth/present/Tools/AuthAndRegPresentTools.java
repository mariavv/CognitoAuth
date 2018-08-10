package com.maria.cognitoauth.present.Tools;

import android.content.Context;

import com.maria.cognitoauth.model.DataParams;
import com.maria.cognitoauth.model.DataSaver;

public class AuthAndRegPresentTools {
    public static final int REGISTER_REQUEST = 3;

    private static final int MIN_LENGTH = 1;
    private static final int PASS_MIN_LENGTH = 6;

    public static boolean isPassCorrect(int passLen) {
        return (passLen >= PASS_MIN_LENGTH);
    }

    public static boolean isParamCorrect(int len) {
        return (len >= MIN_LENGTH);
    }

    public static int getTextLength(String s) {
        return s.length();
    }

    public static void saveData(String phone, String name, String email, String password, Context context) {
        DataSaver.saveParam(DataParams.PHONE, phone, context);
        DataSaver.saveParam(DataParams.NAME, name, context);
        DataSaver.saveParam(DataParams.EMAIL, email, context);
        DataSaver.saveParam(DataParams.PASSWORD, password, context);
    }
}
