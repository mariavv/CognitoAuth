package com.maria.cognitoauth.present;

import android.app.Activity;
import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.AuthView;
import com.maria.cognitoauth.model.DataParams;
import com.maria.cognitoauth.model.DataSaver;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.REGISTER_REQUEST;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.isParamCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.isPassCorrect;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.saveData;

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
        view.close(Activity.RESULT_CANCELED);
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

    @Override
    public void signInSuccessful(String userToken) {
        saveData(null, null, null, null, context);
        DataSaver.saveParam(DataParams.TOKEN, userToken, context);
        view.close();
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
