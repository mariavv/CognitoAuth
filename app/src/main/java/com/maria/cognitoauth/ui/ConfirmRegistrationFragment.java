package com.maria.cognitoauth.ui;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.ConfirmRegistrationView;


public class ConfirmRegistrationFragment extends Fragment implements ConfirmRegistrationView {

    View view;
    EditText codeEd;
    Button confirmBtn;

    public ConfirmRegistrationFragment() {
    }

    public static ConfirmRegistrationFragment newInstance() {
        return new ConfirmRegistrationFragment();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        codeEd = view.findViewById(R.id.confirmCode);
        confirmBtn = view.findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_confirm_registration, container, false);
        return view;
    }
}
