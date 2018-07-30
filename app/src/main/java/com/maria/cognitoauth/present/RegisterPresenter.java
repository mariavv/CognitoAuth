package com.maria.cognitoauth.present;

import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.RegisterView;
import com.maria.cognitoauth.model.DataParams;
import com.maria.cognitoauth.model.DataSaver;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.isParamCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.isPassCorrect;

public class RegisterPresenter implements AuthenticationProvider.SignUpListener {
    private RegisterView view;

    private AuthenticationProvider authProvider;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void textChanged(int phoneLength, int loginLength, int emailLength,
                            int passLength, int confirmPassLength) {
        if (isRegParamsCorrect(phoneLength, loginLength, emailLength, passLength, confirmPassLength)) {
            view.setUpSignBtn(R.string.reg_btn_text_signin,
                    R.color.colorAuthSigninBtnGreen, true);
        } else {
            view.setUpSignBtn(R.string.auth_btn_text_not_fill,
                    R.color.colorAuthSigninBtn, false);
        }
    }

    public void regBtnPressed(String phone, String login, String email, String pass, String confirmPass) {
        if (pass.equals(confirmPass)) {
            authProvider.register(phone, login, email, pass);
        } else {
            view.say(R.string.pass_not_equals);
        }
    }

    public void onClose(String phone, String login, String email, String password, Context context) {
        DataSaver.saveParam(DataParams.PHONE, phone, context);
        DataSaver.saveParam(DataParams.LOGIN, login, context);
        DataSaver.saveParam(DataParams.EMAIL, email, context);
        DataSaver.saveParam(DataParams.PASSWORD, password, context);
        view.say(R.string.regSuccess);
    }

    public void detachView() {
        view = null;
    }

    @Override
    public void onFailure(Exception exception) {
        view.say(exception.getMessage());
    }

    @Override
    public void onRegSuccess(String login) {
        //authProvider.signIn();
        view.close(login);
    }

    @Override
    public void onFailure(int resError) {
        view.say(resError);
    }

    public String getUserId() {
        String userId = authProvider.getUserId();
        if (userId != null) {
            return userId;
        } else {
            return "";
        }
    }

    private boolean isRegParamsCorrect(int nameLen, int loginLen, int emailLen, int passLen, int confirmPassLen) {
        return isParamCorrect(nameLen) && isParamCorrect(loginLen) && isParamCorrect(emailLen)
                && isPassCorrect(passLen) && isPassCorrect(confirmPassLen);
    }
}
