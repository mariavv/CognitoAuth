package com.maria.cognitoauth.ui;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.ProfileView;
import com.maria.cognitoauth.present.ProfilePresenter;

public class ProfileActivity extends AppCompatActivity implements ProfileView {

    private final ProfilePresenter presenter = new ProfilePresenter(this);

    public static Intent start(Context context) {
        return new Intent(context, ProfileActivity.class);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
