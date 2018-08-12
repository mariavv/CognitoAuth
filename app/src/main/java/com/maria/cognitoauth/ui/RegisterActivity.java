package com.maria.cognitoauth.ui;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.Layout;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.RegisterView;
import com.maria.cognitoauth.present.RegisterPresenter;
import com.maria.cognitoauth.ui.Tools.AuthAndRegTools;
import com.maria.cognitoauth.util.Logger;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.getTextLength;
import static com.maria.cognitoauth.ui.Tools.AuthAndRegTools.edGetText;

public class RegisterActivity extends AppCompatActivity implements RegisterView {

    public static final String ARG_LOGIN = "login";
    public static final String ARG_PASSWORD = "password";

    private final RegisterPresenter presenter = new RegisterPresenter(this);

    private EditText phoneEd;
    private EditText nameEd;
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
    public void showConfirmDialog(String userId) {
        signupBtn.setEnabled(false);

        FragmentTransaction trans = getSupportFragmentManager().beginTransaction();
        ConfirmRegistrationFragment confirmFrafment = ConfirmRegistrationFragment.newInstance(userId);
        trans.add(R.id.reg_contanier, confirmFrafment);
        trans.commit();
    }

    @Override
    public void getUseInfo() {
        Logger.log("   <<<get data>>>    ");
        presenter.getUserInfo(phoneEd.getText().toString(), nameEd.getText().toString(),
                emailEd.getText().toString(), passEd.getText().toString());
    }

    @Override
    public void close(int result) {
        Logger.log("   <<<close register activity>>>    ");
        setResult(result, new Intent());
        AuthAndRegTools.finishActivity(this);
    }

    @Override
    public void say(int messageRes) {
        AuthAndRegTools.say(this, messageRes);
    }

    @Override
    public void say(String message) {
        AuthAndRegTools.say(this, message);
    }

    private void configViews() {
        phoneEd = findViewById(R.id.phoneEd);
        nameEd = findViewById(R.id.nameEd);
        emailEd = findViewById(R.id.emailEd);
        passEd = findViewById(R.id.passEd);
        confirmPassEd = findViewById(R.id.confirmPassEd);
        signupBtn = findViewById(R.id.registerBtn);

        phoneEd.setText(getIntent().getStringExtra(ARG_LOGIN));
        passEd.setText(getIntent().getStringExtra(ARG_PASSWORD));
        confirmPassEd.setText(passEd.getText().toString());

        addTextChagedListener(phoneEd);
        addTextChagedListener(nameEd);
        addTextChagedListener(emailEd);
        addTextChagedListener(passEd);
        addTextChagedListener(confirmPassEd);

        signupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.regBtnPressed(edGetText(phoneEd), edGetText(nameEd), edGetText(emailEd),
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
                presenter.textChanged(edGetTextLength(phoneEd), edGetTextLength(nameEd),
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
