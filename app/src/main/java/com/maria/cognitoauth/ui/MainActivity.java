package com.maria.cognitoauth.ui;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.present.MainPresenter;
import com.maria.cognitoauth.iview.MainView;

public class MainActivity extends AppCompatActivity implements MainView {

    private final MainPresenter presenter = new MainPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }
}
