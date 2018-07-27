package com.maria.cognitoauth.present;


import android.content.Context;

import com.maria.cognitoauth.iview.MainView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

import static com.maria.cognitoauth.present.Tools.AuthAndRegTools.REGISTER_REQUEST;

public class MainPresenter implements AuthenticationProvider.SignOutListener, AuthenticationProvider.UserListener {
    private static final int SIGN_IN_REQUEST = 1;
    private static final int PROFILE_REQUEST = 2;

    private MainView view;

    private AuthenticationProvider authProvider;

    public MainPresenter(MainView view) {
        this.view = view;
        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void onCreate(int resGreeting) {
        sayHi(resGreeting);
    }

    public void detachView() {
        view = null;
    }

    public void onResume(int resGreeting) {
        sayHi(resGreeting);
    }

    private void sayHi(int resGreeting) {
        if (authProvider.userSingedIn()) {
            authProvider.getName();
        } else {
            view.changeText(resGreeting);
        }
    }

    public void signInBtnPressed() {
        view.startAuthActivity(SIGN_IN_REQUEST);
    }

    public void signOutBtnPressed() {
        signOut();
    }

    @Override
    public void onFailure(Exception exception) {

    }

    @Override
    public void signOutSuccessful() {

    }

    @Override
    public void getName(String name) {
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

    private void signOut() {
        authProvider.signOut();
    }
}
