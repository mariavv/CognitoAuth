package com.maria.cognitoauth.iview;

public interface AuthView {
    void close(int resultCanceled);

    void setUpSigninBtn(int auth_btn_text_signin, int colorAuthSigninBtnGreen, boolean b);
}
