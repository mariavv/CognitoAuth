package com.maria.cognitoauth.model.network;

import android.content.Context;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;
import com.maria.cognitoauth.R;

import java.util.HashMap;

public class AuthenticationProvider {
    private static final String USER_POOL_ID = "us-east-2_sI1ivFCf3";
    private static final String CLIENT_ID = "2838caidiid33fgpn365ia4bb1";
    private static final String CLIENT_SECRET = "11vuj0q1iuir9rq7pej9l7i9tdim61lnjrbtjguvg1a9ub1hs9ck";
    private static final String REGION = "us-east-2";

    private static final String ATTR_EMAIL = "email";
    private static final String ATTR_USERNAME = "preferred_username";

    private CognitoUserPool userPool;

    private SignUpListener signUpListener;
    private SignInListener signInListener;
    private SignOutListener signOutListener;
    private UserListener userListener;

    public interface Listener extends SignUpListener, SignInListener, SignOutListener, UserListener {
    }

    public interface AuthErrorListener {
        void onFailure(Exception exception);
    }

    public interface SignUpListener extends AuthErrorListener {
        void onRegSuccess();

        void onFailure(int resError);
    }

    public interface SignInListener extends AuthErrorListener {
        void signInSuccessful();
    }

    public interface SignOutListener extends AuthErrorListener {
        void signOutSuccessful();
    }

    public interface UserListener extends AuthErrorListener {
        void getName(String name);
    }

    public AuthenticationProvider(Listener listener, Context context) {
        this.signUpListener = listener;
        this.signInListener = listener;
        this.signOutListener = listener;
        this.userListener = listener;

        createCognitoUserPool(context);
    }

    public AuthenticationProvider(SignUpListener signUpListener, Context context) {
        this.signUpListener = signUpListener;

        createCognitoUserPool(context);
    }

    public AuthenticationProvider(SignInListener signInListener, Context context) {
        this.signInListener = signInListener;

        createCognitoUserPool(context);
    }

    public AuthenticationProvider(SignOutListener signOutListener, Context context) {
        this.signOutListener = signOutListener;

        createCognitoUserPool(context);
    }

    public void signOut() {
        userPool.getCurrentUser().signOut();
        //IdentityManager.getDefaultIdentityManager().signOut();
    }

    public void register(String name, String login, String email, String pass) {
        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        userAttributes.addAttribute(ATTR_EMAIL, email);
        userAttributes.addAttribute(ATTR_USERNAME, name);

        userPool.signUpInBackground(login, pass, userAttributes, null, signUpHandler);
    }

    public void signIn(String login, String pass) {
    }

    public boolean userSingedIn() {
        return false;
        //return IdentityManager.getDefaultIdentityManager().isUserSignedIn();
    }

    public void getName() {
        userPool.getCurrentUser().getDetailsInBackground(getDetailsHandler);
    }

    private void createCognitoUserPool(Context context) {
        userPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, Regions.fromName(REGION));
    }

    private SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            if (!signUpConfirmationState) {
                signUpListener.onRegSuccess();
            } else {
                signUpListener.onFailure(R.string.not_confirmed);
            }
        }

        @Override
        public void onFailure(Exception exception) {
            signUpListener.onFailure(exception);
        }
    };

    private GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
            HashMap map = (HashMap) cognitoUserDetails.getAttributes().getAttributes();
            userListener.getName((String) map.get(ATTR_USERNAME));
        }

        @Override
        public void onFailure(Exception exception) {
            userListener.onFailure(exception);
        }
    };
}
