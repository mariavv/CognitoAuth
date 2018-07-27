package com.maria.cognitoauth.present;

import android.app.Activity;
import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.AuthView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

public class AuthPresenter implements AuthenticationProvider.SignInListener {
    private static final int PASS_MIN_LENGTH = 6;

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

    public void textChanged(final int passLength) {
        if (isAuthParamsCorrect(passLength)) {
            view.setUpSigninBtn(R.string.auth_btn_text_signin,
                    R.color.colorAuthSigninBtnGreen, true);
        } else {
            view.setUpSigninBtn(R.string.auth_btn_text_not_fill,
                    R.color.colorAuthSigninBtn, false);
        }
    }

    public void regBtnPressed(final String login, final String pass) {
        signOut();
        authProvider.register(login, pass);
    }

    public void loginBtnPressed(final String login, String pass) {
        signOut();
        login(login, pass);
    }

    @Override
    public void signInSuccessful() {

    }

    @Override
    public void onRegSuccess() {
        if (authProvider.userExists()) {

        }
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

    private boolean isAuthParamsCorrect(final int passLen) {
        return passLen >= PASS_MIN_LENGTH;
    }
}
