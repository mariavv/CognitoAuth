package com.maria.cognitoauth.iview;

public interface AuthView extends RegisterView {
    void startRegisterActivity(final int reguestCode, String login, String pass);

    void setSigningInState();

    void setSignInState();
}
