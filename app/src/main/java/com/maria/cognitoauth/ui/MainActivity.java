package com.maria.cognitoauth.ui;

import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.present.MainPresenter;
import com.maria.cognitoauth.iview.MainView;

public class MainActivity extends AppCompatActivity implements MainView, NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout drawer;
    private TextView helloView;

    private final MainPresenter presenter = new MainPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initViews();

        presenter.onCreate(R.string.hello_world);
    }

    @Override
    protected void onResume() {
        super.onResume();
        presenter.onResume(R.string.hello_world);
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

    /*@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        MenuItem item = menu.findItem(R.id.app_bar_switch);
        MenuItemCompat.setActionView(item, R.layout.switch_item);
        RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(item);


        return super.onCreateOptionsMenu(menu);
    }*/

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.itemSignIn) {
            presenter.signInBtnPressed();
        } else if (id == R.id.itemSignOut) {
            presenter.signOutBtnPressed();
        } else if (id == R.id.itemExit) {
            presenter.exitBtnPressed();
        }

        drawer.closeDrawer(GravityCompat.START);

        return false;
    }

    @Override
    public void changeText(int resGreeting) {
        helloView.setText(resGreeting);
    }

    @Override
    public void exit() {
        finish();
    }

    @Override
    public void startAuthActivity(final int reguestCode) {
        startActivityForResult(AuthActivity.start(this), reguestCode);
    }

    @Override
    public void startProfileActivity(int reguestCode) {
        startActivityForResult(ProfileActivity.start(this), reguestCode);
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
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                // set item as selected to persist highlight
                menuItem.setChecked(true);
                // close drawer when item is tapped
                drawer.closeDrawers();

                // Add code here to update the UI based on the item selected
                // For example, swap UI fragments here
                int id = menuItem.getItemId();
                if (id == R.id.itemSignIn) {
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
                .getHeaderView(0)
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
