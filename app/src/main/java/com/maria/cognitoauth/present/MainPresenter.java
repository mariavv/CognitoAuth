package com.maria.cognitoauth.present;


import android.content.Context;
import android.content.Intent;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.MainView;
import com.maria.cognitoauth.model.DataParams;
import com.maria.cognitoauth.model.DataSaver;
import com.maria.cognitoauth.model.network.AuthenticationProvider;
import com.maria.cognitoauth.util.Logger;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.REGISTER_REQUEST;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.getUserMinLength;
import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.saveData;
import static com.maria.cognitoauth.ui.Tools.UiTools.getResultOk;

public class MainPresenter implements AuthenticationProvider.AuthListener {
    private static final int SIGN_IN_REQUEST = 1;
    private static final int PROFILE_REQUEST = 2;

    private MainView view;

    private Context context;

    private AuthenticationProvider authProvider;

    public MainPresenter(MainView view) {
        this.view = view;
        this.context = (Context) view;
        authProvider = new AuthenticationProvider(this, (Context) view);
    }

    public void onCreate() {
        checkAuth();
    }

    public void onResume() {
        checkAuth();
    }

    public void detachView() {
        view = null;
    }

    public void signInBtnPressed() {
        view.startAuthActivity(SIGN_IN_REQUEST);
    }

    public void RegisterBtnPressed() {
        view.startRegisterActivity(REGISTER_REQUEST);
    }

    public void signOutBtnPressed() {
        signOut();
    }

    public void exitBtnPressed() {
        view.close();
    }

    public void menuHeaderClick() {
        view.startProfileActivity(PROFILE_REQUEST);
    }

    public void activityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode != getResultOk()) {
            return;
        }

        switch (requestCode) {
            case SIGN_IN_REQUEST:
                setUserInfo(DataSaver.getParam(DataParams.PHONE, context.getString(R.string.header_emptyProfile), context));

                return;
            case REGISTER_REQUEST:
                signIn();

                return;
        }
    }

    @Override
    public void onFailure(Exception exception) {
        view.say(exception.getMessage());
    }

    @Override
    public void signInSuccessful(String userToken, String userId) {
        Logger.log("main presenter: signInSuccessful");
        saveData(userId, null, null, null, userToken, context);
        setUserInfo(userId);
    }

    @Override
    public void signOutSuccessful() {
        view.changeText(R.string.hello_world);
        view.fillProfileInfo(getEmptyProfileString(), getEmptyProfileString(), getEmptyProfileString());
    }

    @Override
    public void onGetUserAttributes(String name, String email) {
        setUserAttributes(name, email);
    }

    private void setUserAttributes(String name, String email) {
        view.setUserAttributes(name, email);
    }

    private String getEmptyProfileString() {
        return context.getString(R.string.header_emptyProfile);
    }

    private void checkAuth() {
        if (authProvider.haveCurrentUser()) {
            Logger.log("main presenter  2 begin" + authProvider.getUserId());
            String userId = authProvider.getUserId();
            setUserInfo(userId);
            Logger.log("main presenter  2 end");
            return;
        }

        signIn();
    }

    private void signIn() {
        String login = DataSaver.getParam(DataParams.PHONE, DataParams.DEF_VALUE, context);
        String password = DataSaver.getParam(DataParams.PASSWORD, DataParams.DEF_VALUE, context);
        Logger.log("main presenter  " + login);
        if (login != null && password != null) {
            Logger.log("main presenter  1 begin");
            authProvider.signIn(login, password);
            Logger.log("main presenter  1 end");
            return;
        }

        view.changeText(R.string.hello_world);
    }

    private void setUserInfo(String userId) {
        view.changeText(userId);
        view.setUser(userId);
        if (userId.length() >= getUserMinLength()) {
            authProvider.getUserAttributes();
        } else {
            setUserAttributes(getEmptyProfileString(), getEmptyProfileString());
        }
    }

    private void signOut() {
        authProvider.signOut();
        setUserInfo(getEmptyProfileString());
    }
}
