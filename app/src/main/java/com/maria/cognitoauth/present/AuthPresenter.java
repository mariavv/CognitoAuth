package com.maria.cognitoauth.present;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.AuthView;
import com.maria.cognitoauth.model.DataParams;
import com.maria.cognitoauth.model.DataSaver;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.REGISTER_REQUEST;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.isParamCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.isPassCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.saveData;
import static com.maria.cognitoauth.ui.Tools.UiTools.getResultCanceled;
import static com.maria.cognitoauth.ui.Tools.UiTools.getResultOk;

public class AuthPresenter implements AuthenticationProvider.SignInListener {

    private AuthView view;

    private Context context;

    private AuthenticationProvider authProvider;

    public AuthPresenter(AuthView view) {
        this.view = view;
        this.context = (Context) view;
        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void detachView() {
        view = null;
    }

    public void backBtnPressed() {
        view.close(getResultCanceled());
    }

    public void textChanged(int loginLength, int passLength) {
        if (isAuthParamsCorrect(loginLength, passLength)) {
            view.setUpSignBtn(R.string.auth_btn_text_signin,
                    R.color.colorAuthSigninBtnGreen, true);
        } else {
            view.setUpSignBtn(R.string.auth_btn_text_not_fill,
                    R.color.colorAuthSigninBtn, false);
        }
    }

    public void regBtnPressed(String login, String pass) {
        view.startRegisterActivity(REGISTER_REQUEST, login, pass);
    }

    public void loginBtnPressed(String login, String pass) {
        signOut();
        login(login, pass);
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getResultOk()) {
            return;
        }

        switch (requestCode) {
            case REGISTER_REQUEST:
                signOut();
                authProvider.signIn(DataSaver.getParam(DataParams.PHONE, null, context),
                        DataSaver.getParam(DataParams.PASSWORD, null, context));

                return;
        }
    }

    @Override
    public void signInSuccessful(String userToken, String userId) {
        saveData(userId, null, null, null, userToken, context);
        view.close(getResultOk());
    }

    @Override
    public void onGetUserAttributes(String name, String email) {

    }

    @Override
    public void onFailure(Exception exception) {
        view.say(exception.getMessage());
        view.setSignInState();
    }

    private void login(String login, String pass) {
        view.setSigningInState();
        authProvider.signIn(login, pass);
    }

    private void signOut() {
        authProvider.signOut();
    }

    private boolean isAuthParamsCorrect(int loginLen, int passLen) {
        return isParamCorrect(loginLen) && isPassCorrect(passLen);
    }
}
