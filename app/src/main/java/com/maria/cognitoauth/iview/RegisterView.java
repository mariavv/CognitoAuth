package com.maria.cognitoauth.iview;

public interface RegisterView extends IView {
    void setUpSignBtn(int btn_text, int colorBtn, boolean enable);

    void showConfirmDialog(String userId);

    void getUseInfo();
}
