package com.maria.cognitoauth.iview;

public interface MainView extends IView {
    void changeText(int resGreeting);

    void startAuthActivity(final int reguestCode);

    void startProfileActivity(final int reguestCode);

    void startRegisterActivity(final int reguestCode);

    void changeText(String name);

    void setUserAttributes(String name, String email);

    void fillProfileInfo(String login, String param, String param1);
}
