package com.maria.cognitoauth.present;


import android.content.Context;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.MainView;
import com.maria.cognitoauth.model.DataParams;
import com.maria.cognitoauth.model.DataSaver;
import com.maria.cognitoauth.model.network.AuthenticationProvider;
import com.maria.cognitoauth.util.Logger;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.REGISTER_REQUEST;

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

    public void signOutBtnPressed() {
        signOut();
    }

    @Override
    public void onFailure(Exception exception) {
        view.say(exception.getMessage());
    }

    @Override
    public void signInSuccessful(String userToken) {
        Logger.log("main presenter: signInSuccessful");
        DataSaver.saveParam(DataParams.TOKEN, userToken, context);
        authProvider.getUserAttributes();
        view.changeText(authProvider.getUserId());
    }

    @Override
    public void signOutSuccessful() {

    }

    @Override
    public void onGetUserAttributes(String name, String email) {
        view.setUserAttributes(name, email);
    }

    public void exitBtnPressed() {
        view.close();
    }

    public void menuHeaderClick() {
        view.startProfileActivity(PROFILE_REQUEST);
    }

    public void RegisterBtnPressed() {
        view.startRegisterActivity(REGISTER_REQUEST);
    }

    private void checkAuth() {
        String login = DataSaver.getParam(DataParams.PHONE, DataParams.DEF_VALUE, context);
        String password = DataSaver.getParam(DataParams.PASSWORD, DataParams.DEF_VALUE, context);
        Logger.log("main presenter  " + login);
        //TODO
        if (login != null) {
            Logger.log("main presenter  1 begin");
            authProvider.signIn(login, password);
            view.changeText(authProvider.getUserId());
            view.fillProfileInfo(login, DataSaver.getParam(DataParams.NAME, "",context),
                    DataSaver.getParam(DataParams.EMAIL, "",context));
            Logger.log("main presenter  1 end");
        } else if (authProvider.isUserSigned()) {
            Logger.log("main presenter  2 begin" + authProvider.getUserId());
            view.changeText(authProvider.getUserId());
            Logger.log("main presenter  2 end");
        //} else if (authProvider.haveCurrentUser()) {
        //    authProvider.signIn(null, password);
        } else {
            view.changeText(R.string.hello_world);
        }
    }

    private void signOut() {
        authProvider.signOut();
    }
}
