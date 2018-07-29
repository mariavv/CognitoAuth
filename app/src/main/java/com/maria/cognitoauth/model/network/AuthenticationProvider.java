package com.maria.cognitoauth.model.network;

import android.content.Context;
;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoDevice;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUser;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserAttributes;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserCodeDeliveryDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserPool;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.CognitoUserSession;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.AuthenticationDetails;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.ChallengeContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.continuations.MultiFactorAuthenticationContinuation;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.AuthenticationHandler;
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

    private SignUpListener signUpListener;
    private SignInListener signInListener;
    private SignOutListener signOutListener;
    private UserListener userListener;

    private CognitoUserPool userPool;

    private String password;

    public interface Listener extends SignUpListener, SignInListener, SignOutListener, UserListener {
    }

    public interface AuthErrorListener {
        void onFailure(Exception exception);
    }

    public interface SignUpListener extends AuthErrorListener {
        void onRegSuccess(String userId);

        void onFailure(int resError);
    }

    public interface SignInListener extends AuthErrorListener {
        void signInSuccessful();
    }

    public interface SignOutListener extends UserListener {
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

        initFields(context);
    }

    public AuthenticationProvider(SignUpListener signUpListener, Context context) {
        this.signUpListener = signUpListener;

        initFields(context);
    }

    public AuthenticationProvider(SignInListener signInListener, Context context) {
        this.signInListener = signInListener;

        initFields(context);
    }

    public AuthenticationProvider(SignOutListener signOutListener, Context context) {
        this.signOutListener = signOutListener;
        this.userListener = signOutListener;

        initFields(context);
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

        this.password = pass;
    }

    public void signIn() {
        userPool.getCurrentUser().getSessionInBackground(handler);
    }

    public void getName() {
        userPool.getCurrentUser().getDetailsInBackground(getDetailsHandler);
    }

    public boolean isUserSigned() {
        return userPool.getCurrentUser().getUserId() != null;
    }

    private void initFields(Context context) {
        createCognitoUserPool(context);

        password = null;
    }

    private void createCognitoUserPool(Context context) {
        userPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, Regions.fromName(REGION));
    }

    private SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            if (!signUpConfirmationState) {
                signUpListener.onRegSuccess(user.getUserId());
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

    private AuthenticationHandler handler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            // Authentication was successful, the "userSession" will have the current valid tokens
            // Time to do awesome stuff
        }

        @Override
        public void getAuthenticationDetails(final AuthenticationContinuation continuation, final String userID) {
            // User authentication details, userId and password are required to continue.
            // Use the "continuation" object to pass the user authentication details

            // After the user authentication details are available, wrap them in an AuthenticationDetails class
            // Along with userId and password, parameters for user pools for Lambda can be passed here
            // The validation parameters "validationParameters" are passed in as a Map<String, String>
            AuthenticationDetails authDetails = new AuthenticationDetails(userID, password, null);

            // Now allow the authentication to continue
            continuation.setAuthenticationDetails(authDetails);
            continuation.continueTask();
        }

        @Override
        public void getMFACode(final MultiFactorAuthenticationContinuation continuation) {
            // Multi-factor authentication is required to authenticate
            // A code was sent to the user, use the code to continue with the authentication


            // Find where the code was sent to
            String codeSentHere = continuation.getParameters().getAttributeName();

            // When the verification code is available, continue to authenticate
            continuation.setMfaCode(codeSentHere);
            continuation.continueTask();
        }

        @Override
        public void authenticationChallenge(final ChallengeContinuation continuation) {
            // A custom challenge has to be solved to authenticate

            // Set the challenge responses

            // Call continueTask() method to respond to the challenge and continue with authentication.
        }

        @Override
        public void onFailure(final Exception exception) {
            // Authentication failed, probe exception for the cause
        }
    };
}
