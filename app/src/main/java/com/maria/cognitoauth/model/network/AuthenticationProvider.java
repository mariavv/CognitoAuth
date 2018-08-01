package com.maria.cognitoauth.model.network;

import android.content.Context;

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
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GenericHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.GetDetailsHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.SignUpHandler;
import com.amazonaws.mobileconnectors.cognitoidentityprovider.handlers.VerificationHandler;
import com.amazonaws.regions.Regions;
import com.maria.cognitoauth.R;

import java.util.Map;


public class AuthenticationProvider {
    private static final String USER_POOL_ID = "us-east-2_sI1ivFCf3";
    private static final String CLIENT_ID = "2838caidiid33fgpn365ia4bb1";
    private static final String CLIENT_SECRET = "11vuj0q1iuir9rq7pej9l7i9tdim61lnjrbtjguvg1a9ub1hs9ck";
    private static final String REGION = "us-east-2";

    private static final String ATTR_EMAIL = "email";
    private static final String ATTR_NAME = "preferred_username";
    private static final String ATTR_PHONE = "phone_number";

    private Listener listener;
    private SignUpListener signUpListener;
    private AuthListener authListener;
    private SignInListener signInListener;

    private CognitoUserPool userPool;

    private String login;
    private String password;

    public interface Listener extends SignUpListener, SignInListener, AuthListener {
    }

    public interface AuthErrorListener {
        void onFailure(Exception exception);
    }

    public interface SignUpListener extends AuthErrorListener {
        void onRegSuccess(/*String userId*/);

        void onFailure(int resError);
    }

    public interface SignInListener extends AuthErrorListener {
        void signInSuccessful(String userToken);
    }

    public interface AuthListener extends SignInListener {
        void signOutSuccessful();

        void onGetUserAttributes(String name, String email);
    }

    public AuthenticationProvider(Listener listener, Context context) {
        this.signUpListener = listener;
        this.signInListener = listener;
        this.authListener = listener;

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

    public AuthenticationProvider(AuthListener authListener, Context context) {
        this.authListener = authListener;

        initFields(context);
    }

    public void signOut() {
        getCurrentUser().signOut();
        //IdentityManager.getDefaultIdentityManager().signOut();
    }

    public void register(String name, String login, String email, String pass) {
        CognitoUserAttributes userAttributes = new CognitoUserAttributes();
        userAttributes.addAttribute(ATTR_EMAIL, email);
        userAttributes.addAttribute(ATTR_NAME, name);
        //userAttributes.addAttribute(ATTR_PHONE, name);

        userPool.signUpInBackground(login, pass, userAttributes, null, signUpHandler);
    }

    public void signIn(String login, String password) {
        this.login = login;
        this.password = password;
        getCurrentUser().getSessionInBackground(handler);
    }

    public void getUserAttributes() {
        getCurrentUser().getDetailsInBackground(getDetailsHandler);
    }

    public boolean isUserSigned() {
        return getUserId() != null;
    }

    public String getUserId() {
        return getCurrentUser().getUserId();
    }

    public boolean haveCurrentUser() {
        return getCurrentUser() != null;
    }

    private void initFields(Context context) {
        createCognitoUserPool(context);

        login = null;
        password = null;
    }

    private CognitoUser getCurrentUser() {
        return userPool.getCurrentUser();
    }

    private void createCognitoUserPool(Context context) {
        userPool = new CognitoUserPool(context, USER_POOL_ID, CLIENT_ID, CLIENT_SECRET, Regions.fromName(REGION));
    }

    private String getAttr(Map attributes, String attr) {
        return attributes.get(attr).toString();
    }

    private SignUpHandler signUpHandler = new SignUpHandler() {
        @Override
        public void onSuccess(CognitoUser user, boolean signUpConfirmationState,
                              CognitoUserCodeDeliveryDetails cognitoUserCodeDeliveryDetails) {
            if (!signUpConfirmationState) {
                resendConfirmationCode(user);
                //user.resendConfirmationCode(ConfHandler);
                user.confirmSignUpInBackground("1", true, confirmationCallback);
                //signUpListener.onRegSuccess(user.getUserId());
            } else {
                onRegSuccess();
            }
        }

        @Override
        public void onFailure(Exception exception) {
            signUpListener.onFailure(exception);
        }
    };

    private void onRegSuccess() {
        signUpListener.onRegSuccess();
    }

    public void resendConfirmationCode(CognitoUser user) {
        user.resendConfirmationCodeInBackground(new VerificationHandler() {
            @Override
            public void onSuccess(CognitoUserCodeDeliveryDetails verificationCodeDeliveryMedium) {
                int c = 5;
                //mCallback.onResendConfirmationCodeSuccess(verificationCodeDeliveryMedium.getDeliveryMedium());
            }

            @Override
            public void onFailure(Exception exception) {
                int c = 5;
                //mCallback.onFailure(PROCESS_RESEND_CONFIRMATION_CODE, exception, CAUSE_UNKNOWN, MESSAGE_UNKNOWN_ERROR);
            }
        });
    }

    private GenericHandler ConfHandler = new GenericHandler() {

        @Override
        public void onSuccess() {
            // Confirmation code was successfully sent!
        }
        @Override
        public void onFailure(Exception exception) {
            // Confirmation code request failed, probe exception for details
        }
    };

    private GenericHandler confirmationCallback = new GenericHandler() {
        @Override
        public void onSuccess() {
            onRegSuccess();
        }

        @Override
        public void onFailure(Exception exception) {
            signUpListener.onFailure(exception);
        }
    };

    private GetDetailsHandler getDetailsHandler = new GetDetailsHandler() {
        @Override
        public void onSuccess(CognitoUserDetails cognitoUserDetails) {
            Map attributes = cognitoUserDetails.getAttributes().getAttributes();
            authListener.onGetUserAttributes(getAttr(attributes, ATTR_NAME), getAttr(attributes, ATTR_EMAIL));
        }

        @Override
        public void onFailure(Exception exception) {
            authListener.onFailure(exception);
        }
    };

    private AuthenticationHandler handler = new AuthenticationHandler() {
        @Override
        public void onSuccess(CognitoUserSession userSession, CognitoDevice newDevice) {
            authListener.signInSuccessful(userSession.getIdToken().getJWTToken());
        }

        @Override
        public void getAuthenticationDetails(final AuthenticationContinuation continuation, final String userID) {
            String userId;
            if (login != null) {
                userId = login;
            } else {
                userId = userID;
            }
            AuthenticationDetails authDetails = new AuthenticationDetails(userId, password, null);

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
            authListener.onFailure(exception);
        }
    };
}
