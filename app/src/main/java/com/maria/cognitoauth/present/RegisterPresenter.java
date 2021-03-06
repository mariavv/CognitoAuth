package com.maria.cognitoauth.present;

import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.RegisterView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;
import com.maria.cognitoauth.util.Logger;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.isParamCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.isPassCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.saveData;

public class RegisterPresenter implements AuthenticationProvider.SignUpListener {
    private RegisterView view;

    private Context context;

    private AuthenticationProvider authProvider;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        this.context = (Context) view;
        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void textChanged(int phoneLength, int nameLength, int emailLength,
                            int passLength, int confirmPassLength) {
        if (isRegParamsCorrect(phoneLength, nameLength, emailLength, passLength, confirmPassLength)) {
            view.setUpSignBtn(R.string.reg_btn_text_signin,
                    R.color.colorAuthSigninBtnGreen, true);
        } else {
            view.setUpSignBtn(R.string.auth_btn_text_not_fill,
                    R.color.colorAuthSigninBtn, false);
        }
    }

    public void regBtnPressed(String phone, String name, String email, String pass, String confirmPass) {
        if (pass.equals(confirmPass)) {
            authProvider.register(phone, name, email, pass);
        } else {
            view.say(R.string.pass_not_equals);
        }
    }

    public void detachView() {
        view = null;
    }

    @Override
    public void onFailure(Exception exception) {
        view.say(exception.getMessage());
    }

    @Override
    public void onRegSuccess(String userId) {
        view.say(R.string.regSuccess);
        view.getUseInfo();
        view.showConfirmDialog(userId);
    }

    @Override
    public void onFailure(int resError) {
        view.say(resError);
    }

    @Override
    public void onConfirmRegSuccess() {
        //do nothing
    }

    private boolean isRegParamsCorrect(int nameLen, int loginLen, int emailLen, int passLen, int confirmPassLen) {
        return isParamCorrect(nameLen) && isParamCorrect(loginLen) && isParamCorrect(emailLen)
                && isPassCorrect(passLen) && isPassCorrect(confirmPassLen);
    }

    public void getUserInfo(String phone, String name, String email, String password) {
        saveData(phone, name, email, password, null, context);
    }
}
