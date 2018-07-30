package com.maria.cognitoauth.present;


import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.MainView;
import com.maria.cognitoauth.model.DataParams;
import com.maria.cognitoauth.model.DataSaver;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.REGISTER_REQUEST;

public class MainPresenter implements AuthenticationProvider.AuthListener {
    private static final int SIGN_IN_REQUEST = 1;
    private static final int PROFILE_REQUEST = 2;

    private MainView view;

    private AuthenticationProvider authProvider;

    public MainPresenter(MainView view) {
        this.view = view;
        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void onCreate(Context context) {
        checkAuth(context);
    }

    public void onResume(Context context) {
        checkAuth(context);
    }

    public void detachView() {
        view = null;
    }

    public void signInBtnPressed() {
        view.startAuthActivity(SIGN_IN_REQUEST);
    }

    public void signOutBtnPressed() {
        signOut();
    }

    @Override
    public void onFailure(Exception exception) {
        view.say(exception.getMessage());
    }

    @Override
    public void signInSuccessful() {
        authProvider.getUserAttributes();
    }

    @Override
    public void signOutSuccessful() {

    }

    @Override
    public void onGetUserAttributes(String name, String email) {
        view.setUserAttributes(name, email);
        view.changeText(name);
    }

    public void exitBtnPressed() {
        view.close();
    }

    public void menuHeaderClick() {
        view.startProfileActivity(PROFILE_REQUEST);
    }

    public void RegisterBtnPressed() {
        view.startRegisterActivity(REGISTER_REQUEST);
    }

    private void checkAuth(Context context) {
        String login = DataSaver.getParam(DataParams.LOGIN, DataParams.DEF_VALUE, context);
        String password = DataSaver.getParam(DataParams.PASSWORD, DataParams.DEF_VALUE, context);
        //if (userId != null) {
        //TODO
        if (authProvider.isUserSigned()) {
            view.changeText(authProvider.getUserId());
        } else if (login != null) {
            authProvider.signIn(login, password);
        } else if (authProvider.haveCurrentUser()) {
            authProvider.signIn(null, password);
        } else {
            view.changeText(R.string.hello_world);
        }
    }

    private void signOut() {
        authProvider.signOut();
    }
}
