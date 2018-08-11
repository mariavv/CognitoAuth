package com.maria.cognitoauth.iview;

public interface IView {
    void close(int resultCode);

    void say(int messageRes);

    void say(String message);
}
