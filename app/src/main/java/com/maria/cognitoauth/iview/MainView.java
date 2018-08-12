package com.maria.cognitoauth.iview;

public interface MainView extends IView {
    void close();

    void setGreeting(int resGreeting);

    void startAuthActivity(final int reguestCode);

    void startProfileActivity(final int reguestCode);

    void startRegisterActivity(final int reguestCode);

    void setGreeting(String name);

    void setUserAttributes(String name, String email);

    void fillProfileInfo(String phone, String name, String email);

    void setUser(String phone);
}
