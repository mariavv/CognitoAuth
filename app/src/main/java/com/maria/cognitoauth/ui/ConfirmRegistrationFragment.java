package com.maria.cognitoauth.ui;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.maria.cognitoauth.R;
import com.maria.cognitoauth.iview.ConfirmRegistrationView;
import com.maria.cognitoauth.present.ConfirmRegistrationPresenter;
import com.maria.cognitoauth.ui.Tools.AuthAndRegTools;

import java.util.Objects;


public class ConfirmRegistrationFragment extends Fragment implements ConfirmRegistrationView {

    private static final String ARG_USER_ID = "user_id";

    ConfirmRegistrationPresenter presenter;

    View view;
    EditText codeEd;
    Button confirmBtn;

    private String userId;

    public ConfirmRegistrationFragment() {
    }

    public static ConfirmRegistrationFragment newInstance(String userId) {
        ConfirmRegistrationFragment fragment = new ConfirmRegistrationFragment();
        Bundle args = new Bundle();
        args.putString(ARG_USER_ID, userId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            userId = getArguments().getString(ARG_USER_ID);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_confirm_registration, container, false);

        presenter = new ConfirmRegistrationPresenter(this, getContext());

        configView();

        return view;
    }

    private void configView() {
        codeEd = view.findViewById(R.id.confirmCode);
        confirmBtn = view.findViewById(R.id.confirmBtn);

        confirmBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                presenter.onBtnClick(codeEd.getText().toString(), userId);
            }
        });
    }


    @Override
    public void onDestroy() {
        presenter.detachView();
        super.onDestroy();
    }

    @Override
    public void close(int resultCode) {
        getActivity().setResult(resultCode);
        Objects.requireNonNull(getActivity()).finish();
    }

    @Override
    public void say(int messageRes) {
        AuthAndRegTools.say(getContext(), messageRes);
    }

    @Override
    public void say(String message) {
        AuthAndRegTools.say(getContext(), message);
    }
}
