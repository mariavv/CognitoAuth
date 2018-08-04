package com.maria.cognitoauth.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.TypedValue;
import android.view.Display;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.AuthView;
import com.maria.cognitoauth.present.AuthPresenter;
import com.maria.cognitoauth.ui.Tools.AuthAndRegTools;

import ru.tinkoff.decoro.MaskImpl;
import ru.tinkoff.decoro.parser.UnderscoreDigitSlotsParser;
import ru.tinkoff.decoro.slots.Slot;
import ru.tinkoff.decoro.watchers.FormatWatcher;
import ru.tinkoff.decoro.watchers.MaskFormatWatcher;

import static com.maria.cognitoauth.present.Tools.AuthAndRegPresentTools.getTextLength;
import static com.maria.cognitoauth.ui.Tools.AuthAndRegTools.edGetText;

public class AuthActivity extends AppCompatActivity implements AuthView {
    private static final String RES_IDENTIFIER_NAME_STATUS_BAR_HEIGHT = "status_bar_height";
    private static final String DEF_TYPE_DIMEN = "dimen";
    private static final String DEF_PACKAGE_ANDROID = "android";

    private static final String PHONE_MASK = "+7 (___) ___ ____";

    private final AuthPresenter presenter = new AuthPresenter(this);

    private EditText loginEd;
    private EditText passEd;
    private Button signinBtn;

    public static Intent start(Context context) {
        return new Intent(context, AuthActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auth);

        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        }
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        configViews();

        fillScroll(); // позволяет растянуть scrollView на высоту экрана
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            presenter.backBtnPressed();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void close(int result) {
        setResult(result, new Intent());
        AuthAndRegTools.finishActivity(this);
    }

    @Override
    public void close() {
        //do nothing
    }

    @Override
    public void say(int messageRes) {
        AuthAndRegTools.say(this, messageRes);
    }

    @Override
    public void say(String message) {
        AuthAndRegTools.say(this, message);
    }

    @Override
    public void setUpSignBtn(int resText, int resColor, boolean enabled) {
        signinBtn.setText(resText);
        signinBtn.setBackgroundColor(this.getResources().getColor(resColor));
        signinBtn.setEnabled(enabled);
    }

    @Override
    public void close(String userId) {

    }

    @Override
    public void showConfirmDialog() {

    }

    @Override
    public void startRegisterActivity(int reguestCode, String login, String pass) {
        startActivityForResult(RegisterActivity.start(this, login, pass), reguestCode);
    }

    @Override
    public void getToken(String userToken) {
        presenter.onGetToken(userToken, this);
    }

    private void configViews() {
        loginEd = findViewById(R.id.loginEd);
        passEd = findViewById(R.id.passEd);
        signinBtn = findViewById(R.id.loginBtn);

        //TODO
        //addPhoneEdMask();

        addTextChagedListener(loginEd);
        addTextChagedListener(passEd);

        findViewById(R.id.regBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.regBtnPressed(edGetText(loginEd), edGetText(passEd));
            }
        });

        signinBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.loginBtnPressed(edGetText(loginEd), edGetText(passEd));
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
                presenter.textChanged(edGetTextLength(loginEd), edGetTextLength(passEd));
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    // Растягивает view на весь экран
    private void fillScroll() {
        // Высота экрана
        Display display = getWindowManager().getDefaultDisplay();
        Point size = new Point();
        display.getSize(size);

        // Высота ActionBar
        TypedValue tv = new TypedValue();
        int actionBarHeight = 0;
        if (getTheme().resolveAttribute(android.R.attr.actionBarSize, tv, true)) {
            actionBarHeight = TypedValue.complexToDimensionPixelSize(tv.data, getResources().getDisplayMetrics());
        }

        // Высота строки состояния
        int stateBarHeight = 0;
        int resourceId = getResources().getIdentifier(RES_IDENTIFIER_NAME_STATUS_BAR_HEIGHT, DEF_TYPE_DIMEN, DEF_PACKAGE_ANDROID);
        if (resourceId > 0) {
            stateBarHeight = getResources().getDimensionPixelSize(resourceId);
        }

        ImageView glassyLayout = findViewById(R.id.glassyLayout);
        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) glassyLayout.getLayoutParams();
        params.height = size.y - actionBarHeight - stateBarHeight;
        glassyLayout.requestLayout();
    }

    private void addPhoneEdMask() {
        Slot[] slots = new UnderscoreDigitSlotsParser().parseSlots(PHONE_MASK);
        FormatWatcher formatWatcher = new MaskFormatWatcher(MaskImpl.createTerminated(slots));
        formatWatcher.installOn(loginEd);
    }

    private int edGetTextLength(EditText ed) {
        return getTextLength(ed.getText().toString());
    }
}
