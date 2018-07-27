package com.maria.cognitoauth.present;

import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.RegisterView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

import static com.maria.cognitoauth.present.Tools.AuthTools.isParamCorrect;
import static com.maria.cognitoauth.present.Tools.AuthTools.isPassCorrect;

public class RegisterPresenter implements AuthenticationProvider.SignUpListener{
    private RegisterView view;

    private AuthenticationProvider authProvider;

    public RegisterPresenter(RegisterView view) {
        this.view = view;
        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void textChanged(final int nameLength, final int loginLength, final int emailLength, final int passLength) {
        if (isRegParamsCorrect(nameLength, loginLength, emailLength, passLength)) {
            view.setUpSignupBtn(R.string.reg_btn_text_signin,
                    R.color.colorAuthSigninBtnGreen, true);
        } else {
            view.setUpSignupBtn(R.string.auth_btn_text_not_fill,
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

    }

    private boolean isRegParamsCorrect(int nameLen, int loginLen, int emailLen, int passLen) {
        return isParamCorrect(nameLen) && isParamCorrect(loginLen) && isParamCorrect(emailLen) && isPassCorrect(passLen);
    }
}
