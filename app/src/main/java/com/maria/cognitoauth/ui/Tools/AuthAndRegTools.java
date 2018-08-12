package com.maria.cognitoauth.ui.Tools;

import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.widget.EditText;
import android.widget.Toast;

import com.maria.cognitoauth.util.Logger;

public class AuthAndRegTools {
    public static String edGetText(EditText ed) {
        return ed.getText().toString();
    }

    public static void finishActivity(Activity activity) {
        activity.finish();
    };

    public static void say(Context context, int messageRes) {
        say(context, context.getString(messageRes));
    }

    public static void say(Context context, String message) {
        Logger.log("   <MESSAGE>    " + message);
        Toast toast = Toast.makeText(context, message, Toast.LENGTH_SHORT);
        toast.setGravity(Gravity.CENTER_VERTICAL, 0, 0);
        toast.show();
    }
}
