package com.maria.cognitoauth.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.Button;
import android.widget.EditText;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.RegisterView;
import com.maria.cognitoauth.present.RegisterPresenter;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    private final RegisterPresenter presenter = new RegisterPresenter(this);

    private EditText nameEd;
    private EditText loginEd;
    private EditText emailEd;
    private EditText passEd;
    private Button signupBtn;

    public static Intent start(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        configViews();
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void setUpSignupBtn(int resText, int resColor, boolean enabled) {
        signupBtn.setText(resText);
        signupBtn.setBackgroundColor(this.getResources().getColor(resColor));
        signupBtn.setEnabled(enabled);
    }

    private void configViews() {
        nameEd = findViewById(R.id.nameEd);
        loginEd = findViewById(R.id.loginEd);
        emailEd = findViewById(R.id.emailEd);
        passEd = findViewById(R.id.passEd);
        signupBtn = findViewById(R.id.registerBtn);

        addTextChagedListener(nameEd);
        addTextChagedListener(loginEd);
        addTextChagedListener(emailEd);
        addTextChagedListener(passEd);
    }

    private void addTextChagedListener(EditText ed) {
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.textChanged(edGetTextLength(nameEd), edGetTextLength(loginEd),
                        edGetTextLength(emailEd), edGetTextLength(passEd));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private int edGetTextLength(EditText ed) {
        return ed.getText().length();
    }
}
