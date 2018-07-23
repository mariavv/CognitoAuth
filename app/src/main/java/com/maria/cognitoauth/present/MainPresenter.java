package com.maria.cognitoauth.present;

import android.content.Context;

import com.maria.cognitoauth.iview.MainView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

public class MainPresenter implements AuthenticationProvider.Listener{
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
        if (/*authProvider.userExists()*/false) {
            //view.changeText((new AuthManager(this)).getEmail());
        } else {
            view.changeText(resGreeting);
        }
    }
}
