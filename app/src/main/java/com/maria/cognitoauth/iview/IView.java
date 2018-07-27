package com.maria.cognitoauth.iview;

public interface IView {
    void close();

    void say(int messageRes);

    void say(String message);
}
