package com.maria.cognitoauth.model.network;

public class AuthenticationProvider {
    private Listener listener;

    public interface Listener {
    }

    public AuthenticationProvider(Listener listener) {
        this.listener = listener;
    }
}
