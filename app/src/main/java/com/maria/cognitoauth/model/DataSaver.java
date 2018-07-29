package com.maria.cognitoauth.model;

import android.content.Context;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class DataSaver {

    @Nullable
    public static String getParam(String param, @Nullable String defValue, Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context).getString(param, defValue);
    }

    public static void saveParam(String param, String value, Context context) {
        PreferenceManager.getDefaultSharedPreferences(context)
                .edit()
                .putString(param, value)
                .apply();
    }
}
