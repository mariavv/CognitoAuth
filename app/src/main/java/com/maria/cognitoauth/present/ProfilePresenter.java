package com.maria.cognitoauth.present;

import com.maria.cognitoauth.iview.ProfileView;

public class ProfilePresenter {
    private ProfileView view;

    public ProfilePresenter(ProfileView view) {
        this.view = view;
    }

    public void detachView() {
        view = null;
    }
}
