package com.maria.cognitoauth.present;

import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.RegisterView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

import static com.maria.cognitoauth.present.Tools.AuthAndRegTools.isParamCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegTools.isPassCorrect;

public class RegisterPresenter implements AuthenticationProvider.SignUpListener {
    private RegisterView view;

    private AuthenticationProvider authProvider;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void textChanged(final int nameLength, final int loginLength, final int emailLength,
                            final int passLength, final int confirmPassLength) {
        if (isRegParamsCorrect(nameLength, loginLength, emailLength, passLength, confirmPassLength)) {
            view.setUpSignBtn(R.string.reg_btn_text_signin,
                    R.color.colorAuthSigninBtnGreen, true);
        } else {
            view.setUpSignBtn(R.string.auth_btn_text_not_fill,
                    R.color.colorAuthSigninBtn, false);
        }
    }

    public void detachView() {
        view = null;
    }

    @Override
    public void onFailure(Exception exception) {

    }

    @Override
    public void onRegSuccess() {
        view.close();
    }

    @Override
    public void onFailure(final int resError) {
        view.say(resError);
    }

    private boolean isRegParamsCorrect(final int nameLen, final int loginLen, final int emailLen,
                                       final int passLen, final int confirmPassLen) {
        return isParamCorrect(nameLen) && isParamCorrect(loginLen) && isParamCorrect(emailLen)
                && isPassCorrect(passLen) && isPassCorrect(confirmPassLen);
    }

    public void regBtnPressed(final String name, final String login, final String email,
                              final String pass, final String confirmPass) {
        if (pass.equals(confirmPass)) {
            authProvider.register(name, login, email, pass);
        } else {
            view.say(R.string.pass_not_equals);
        }
    }
}
