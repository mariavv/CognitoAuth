package com.maria.cognitoauth.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.RegisterView;
import com.maria.cognitoauth.present.RegisterPresenter;
import com.maria.cognitoauth.ui.Tools.AuthAndRegTools;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.getTextLength;
import static com.maria.cognitoauth.ui.Tools.AuthAndRegTools.edGetText;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    public static final String ARG_LOGIN = "login";
    public static final String ARG_PASSWORD = "password";

    private final RegisterPresenter presenter = new RegisterPresenter(this);

    private EditText phoneEd;
    private EditText loginEd;
    private EditText emailEd;
    private EditText passEd;
    private EditText confirmPassEd;
    private Button signupBtn;

    public static Intent start(Context context) {
        return new Intent(context, RegisterActivity.class);
    }

    public static Intent start(Context context, String login, String pass) {
        Intent intent = new Intent(context, RegisterActivity.class);

        Bundle arguments = new Bundle();
        arguments.putString(ARG_LOGIN, login);
        arguments.putString(ARG_PASSWORD, pass);
        intent.putExtras(arguments);

        return intent;
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
    public void setUpSignBtn(int resText, int resColor, boolean enabled) {
        signupBtn.setText(resText);
        signupBtn.setBackgroundColor(this.getResources().getColor(resColor));
        signupBtn.setEnabled(enabled);
    }

    @Override
    public void close(String login) {
        presenter.onClose(phoneEd.getText().toString(), loginEd.getText().toString(),
                emailEd.getText().toString(), passEd.getText().toString(), this);
        finishActivity();
    }

    @Override
    public void close() {
        finishActivity();
    }

    @Override
    public void say(int messageRes) {
        AuthAndRegTools.say(this, messageRes);
    }

    @Override
    public void say(String message) {
        AuthAndRegTools.say(this, message);
    }

    private void finishActivity() {
        AuthAndRegTools.finishActivity(this);
    }

    private void configViews() {
        phoneEd = findViewById(R.id.phoneEd);
        loginEd = findViewById(R.id.loginEd);
        emailEd = findViewById(R.id.emailEd);
        passEd = findViewById(R.id.passEd);
        confirmPassEd = findViewById(R.id.confirmPassEd);
        signupBtn = findViewById(R.id.registerBtn);

        loginEd.setText(getIntent().getStringExtra(ARG_LOGIN));
        passEd.setText(getIntent().getStringExtra(ARG_PASSWORD));

        //todo
        phoneEd.setText("89001004000");
        loginEd.setText("qwerty111");
        emailEd.setText("testproj28@gmail.ru");
        passEd.setText("Qwerty12");
        confirmPassEd.setText("Qwerty12");

        addTextChagedListener(phoneEd);
        addTextChagedListener(loginEd);
        addTextChagedListener(emailEd);
        addTextChagedListener(passEd);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.regBtnPressed(edGetText(phoneEd), edGetText(loginEd), edGetText(emailEd),
                        edGetText(passEd), edGetText(confirmPassEd));
            }
        });
    }

    private void addTextChagedListener(EditText ed) {
        ed.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                presenter.textChanged(edGetTextLength(phoneEd), edGetTextLength(loginEd),
                        edGetTextLength(emailEd), edGetTextLength(passEd), edGetTextLength(confirmPassEd));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private int edGetTextLength(EditText ed) {
        return getTextLength(ed.getText().toString());
    }
}
