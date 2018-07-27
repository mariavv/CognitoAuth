package com.maria.cognitoauth.present;

import android.app.Activity;
import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.AuthView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

import static com.maria.cognitoauth.present.Tools.AuthAndRegTools.REGISTER_REQUEST;
import static com.maria.cognitoauth.present.Tools.AuthAndRegTools.isParamCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegTools.isPassCorrect;

public class AuthPresenter implements AuthenticationProvider.SignInListener {

    private AuthView view;

    private AuthenticationProvider authProvider;

    public AuthPresenter(AuthView view) {
        this.view = view;

        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void detachView() {
        view = null;
    }

    public void backBtnPressed() {
        view.close(Activity.RESULT_CANCELED);
    }

    public void textChanged(int loginLength, final int passLength) {
        if (isAuthParamsCorrect(loginLength, passLength)) {
            view.setUpSignBtn(R.string.auth_btn_text_signin,
                    R.color.colorAuthSigninBtnGreen, true);
        } else {
            view.setUpSignBtn(R.string.auth_btn_text_not_fill,
                    R.color.colorAuthSigninBtn, false);
        }
    }

    public void regBtnPressed(final String login, final String pass) {
        view.startRegisterActivity(REGISTER_REQUEST, login, pass);
    }

    public void loginBtnPressed(final String login, String pass) {
        signOut();
        login(login, pass);
    }

    @Override
    public void signInSuccessful() {

    }

    @Override
    public void onFailure(Exception exception) {

    }

    private void login(final String login, String pass) {
        authProvider.signIn(login, pass);
    }

    private void signOut() {
        authProvider.signOut();
    }

    private boolean isAuthParamsCorrect(int loginLen, int passLen) {
        return isParamCorrect(loginLen) && isPassCorrect(passLen);
    }
}
