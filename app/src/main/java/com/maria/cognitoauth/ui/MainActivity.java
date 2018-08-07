package com.maria.cognitoauth.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.MainView;
import com.maria.cognitoauth.present.MainPresenter;
import com.maria.cognitoauth.ui.Tools.AuthAndRegTools;

public class MainActivity extends AppCompatActivity implements MainView {

    private static final int SIDE_BAR_HEADER_VIEW_INDEX = 0;

    private DrawerLayout drawer;
    private TextView helloView;

    private TextView tvLogin;
    private TextView tvPhone;
    private TextView tvEmail;

    private final MainPresenter presenter = new MainPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        presenter.onCreate(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        //todo presenter.onResume(this);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void onBackPressed() {
        int dir = GravityCompat.START;
        if (drawer.isDrawerOpen(dir)) {
            drawer.closeDrawer(dir);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public void changeText(int resGreeting) {
        helloView.setText(resGreeting);
    }

    @Override
    public void changeText(String name) {
        helloView.setText(String.format(getString(R.string.hello_user), name));
        //setText(name);
    }

    @Override
    public void close() {
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

    @Override
    public void startAuthActivity(int reguestCode) {
        startActivityForResult(AuthActivity.start(this), reguestCode);
    }

    @Override
    public void startProfileActivity(int reguestCode) {
        startActivityForResult(ProfileActivity.start(this), reguestCode);
    }

    @Override
    public void startRegisterActivity(int reguestCode) {
        startActivityForResult(RegisterActivity.start(this), reguestCode);
    }

    @Override
    public void setUserAttributes(String name, String email) {
        //todo show and save
    }

    @Override
    public void fillProfileInfo(String login, String phone, String email) {

    }

    private void initViews() {
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        drawer = findViewById(R.id.drawerLayout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar,
                R.string.sideBar_navigation_drawer_open, R.string.sideBar_navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);

        View header = navigationView.getHeaderView(0);
        tvLogin = header.findViewById(R.id.tvPhone);
        tvPhone = header.findViewById(R.id.tvName);
        tvEmail = header.findViewById(R.id.tvEmail);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                drawer.closeDrawers();

                int id = menuItem.getItemId();
                if (id == R.id.itemRegister) {
                    presenter.RegisterBtnPressed();
                } else if (id == R.id.itemSignIn) {
                        presenter.signInBtnPressed();
                } else if (id == R.id.itemSignOut) {
                    presenter.signOutBtnPressed();
                } else if (id == R.id.itemExit) {
                    presenter.exitBtnPressed();
                }
                return true;
            }
        });

        navigationView
                .getHeaderView(SIDE_BAR_HEADER_VIEW_INDEX)
                .findViewById(R.id.menuHeaderLayout)
                .setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        presenter.menuHeaderClick();
                        drawer.closeDrawers();
                    }
                });

        helloView = findViewById(R.id.helloView);
    }
}
