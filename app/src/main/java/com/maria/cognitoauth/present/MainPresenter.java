package com.maria.cognitoauth.present;

import com.maria.cognitoauth.iview.MainView;

public class MainPresenter {
    private MainView view;

    public MainPresenter(MainView view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }
}
