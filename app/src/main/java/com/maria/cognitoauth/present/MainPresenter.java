package com.maria.cognitoauth.present;

import android.content.Context;

import com.maria.cognitoauth.iview.MainView;
import com.maria.cognitoauth.model.network.AuthenticationProvider;

public class MainPresenter implements AuthenticationProvider.Listener{
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void onCreate() {
        AuthenticationProvider authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void detachView() {
        view = null;
    }
}
