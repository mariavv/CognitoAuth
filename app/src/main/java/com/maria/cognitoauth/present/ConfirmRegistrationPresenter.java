package com.maria.cognitoauth.present;

import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.ConfirmRegistrationView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

public class ConfirmRegistrationPresenter implements AuthenticationProvider.SignUpListener {
    private ConfirmRegistrationView view;

    AuthenticationProvider authProvider;

    public ConfirmRegistrationPresenter(ConfirmRegistrationView view, Context context) {
        this.view = view;

        authProvider = new AuthenticationProvider(this, context);
    }

    public void detachView() {
        view = null;
    }

    public void onBtnClick(String code, String userId) {
        authProvider.confirmReg(code, userId);
    }

    @Override
    public void onFailure(Exception exception) {
        view.say(exception.getMessage());
    }

    @Override
    public void onRegSuccess(String userId) {
        //do nothing
    }

    @Override
    public void onFailure(int resError) {
        view.say(resError);
    }

    @Override
    public void onConfirmRegSuccess() {
        view.say(R.string.confirm_success);
        view.close();
    }
}
