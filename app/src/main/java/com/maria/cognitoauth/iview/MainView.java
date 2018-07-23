package com.maria.cognitoauth.iview;

public interface MainView {
    void changeText(int resGreeting);

    void exit();

    void startAuthActivity(final int reguestCode);
}
