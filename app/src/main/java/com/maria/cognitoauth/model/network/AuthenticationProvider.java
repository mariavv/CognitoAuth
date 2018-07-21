package com.maria.cognitoauth.model.network;

import android.content.Context;

import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.regions.Regions;

public class AuthenticationProvider {
    private static final String USER_POOL_ID = "us-east-2_sI1ivFCf3";
    private static final String CLIENT_ID = "2838caidiid33fgpn365ia4bb1";
    private static final String CLIENT_SECRET = "11vuj0q1iuir9rq7pej9l7i9tdim61lnjrbtjguvg1a9ub1hs9ck";
    private static final String REGION = "us-east-2";

    private Listener listener;

    CognitoUserPool userPool;

    public interface Listener {
    }

    public AuthenticationProvider(Listener listener, Context context) {
        this.listener = listener;

        createCognitoUserPool(context);
    }

    private void createCognitoUserPool(Context context) {
        userPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, Regions.fromName(REGION));
    }
}
