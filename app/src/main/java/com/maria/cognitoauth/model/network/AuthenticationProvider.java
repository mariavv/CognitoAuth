package com.maria.cognitoauth.model.network;

import android.content.Context;

import com.amazonaws.mobile.auth.core.IdentityManager;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.regions.Regions;

public class AuthenticationProvider {
    private static final String USER_POOL_ID = "us-east-2_sI1ivFCf3";
    private static final String CLIENT_ID = "2838caidiid33fgpn365ia4bb1";
    private static final String CLIENT_SECRET = "11vuj0q1iuir9rq7pej9l7i9tdim61lnjrbtjguvg1a9ub1hs9ck";
    private static final String REGION = "us-east-2";

    private static final String ATTR_EMAIL = "email";
    private static final String ATTR_USERNAME = "preferred_username";
    private static final String ATTR_PHONE = "phone";

    private CognitoUserPool userPool;

    //private Listener listener;
    private SignUpListener signUpListener;
    private SignInListener signInListener;
    private SignOutListener signOutListener;

    /*public interface Listener {
        void onRegSuccess();

        void onRegFailure(Exception exception);
    }*/

    public interface Listener extends SignUpListener, SignInListener, SignOutListener {
    }

    public interface AuthErrorListener {
        void onFailure(Exception exception);
    }

    public interface SignUpListener extends AuthErrorListener {
        void onRegSuccess();
    }

    public interface SignInListener extends AuthErrorListener {
        void signInSuccessful();
    }

    public interface SignOutListener extends AuthErrorListener {
        void signOutSuccessful();
    }

    public AuthenticationProvider(Listener listener, Context context) {
        this.signUpListener = listener;
        this.signInListener = listener;
        this.signOutListener = listener;
        //this.listener = listener;

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

    public AuthenticationProvider(SignOutListener signOutListener) {
        this.signOutListener = signOutListener;
    }

    public void signOut() {
        userPool.getCurrentUser().signOut();
    }

    public void register(String login, String pass) {
        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        userAttributes.addAttribute(ATTR_EMAIL, "login@mail.ru");
        userAttributes.addAttribute(ATTR_USERNAME, "89001112222");

        userPool.signUpInBackground(login, pass, userAttributes, null, signUpHandler);
    }

    public void signIn(String login, String pass) {
    }

    public boolean userExists() {
        return IdentityManager.getDefaultIdentityManager().isUserSignedIn();
    }

    private void createCognitoUserPool(Context context) {
        userPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, Regions.fromName(REGION));
    }

    private SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            signUpListener.onRegSuccess();
        }

        @Override
        public void onFailure(Exception exception) {
            signInListener.onFailure(exception);
        }
    };
}
