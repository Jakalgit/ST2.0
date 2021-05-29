package com.example.soundtranslator.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soundtranslator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

;
import static com.example.soundtranslator.Methods.hideKeyBoard;
import static com.example.soundtranslator.Methods.isOnline;
import static com.example.soundtranslator.Methods.writeNewUser;


public class FragmentRegister extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;
    private String email, username, passwordd;

    private Button regButt;

    private FirebaseAuth mAuth;
    private DatabaseReference mDatabase;
    private DatabaseReference ref;

    private EditText Email, userName, password;

    private MotionLayout register;

    private TextView emailErr, nameErr, passErr, txtErr;

    private ImageView btnBack;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View rootView = inflater.inflate(R.layout.fragment_register, container, false);

        regButt = rootView.findViewById(R.id.regButt);
        btnBack = rootView.findViewById(R.id.btnBack2);

        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference();
        ref = FirebaseDatabase.getInstance().getReference().child("Users");

        Email = rootView.findViewById(R.id.email);
        userName = rootView.findViewById(R.id.username);
        password = rootView.findViewById(R.id.password1);

        emailErr = rootView.findViewById(R.id.emailErr);
        nameErr = rootView.findViewById(R.id.nameErr);
        passErr = rootView.findViewById(R.id.passErr);
        txtErr = rootView.findViewById(R.id.txtErr);

        register = rootView.findViewById(R.id.register);

        btnBack.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentSignIn());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        regButt.setOnClickListener(v -> {
            boolean flag = false;
            email = Email.getText().toString();
            if (email.isEmpty()) {
                emailErr.setText("Адрес не может быть пустым");
                emailErr.setVisibility(View.VISIBLE);
                flag = false;
            } else {
                emailErr.setVisibility(View.INVISIBLE);
                flag = true;
            }

            username = userName.getText().toString();
            if (username.isEmpty()) {
                nameErr.setText("Имя не может быть путстым");
                nameErr.setVisibility(View.VISIBLE);
                flag = false;
            } else {
                nameErr.setVisibility(View.INVISIBLE);
                flag = true;
            }

            if(username.length() < 2){
                nameErr.setText("Имя пользователя от 2 символов");
                nameErr.setVisibility(View.VISIBLE);
                flag = false;
            } else {
                nameErr.setVisibility(View.INVISIBLE);
                flag = true;
            }

            passwordd = password.getText().toString();
            if (passwordd.length() < 6) {
                passErr.setVisibility(View.VISIBLE);
                flag = false;
            } else {
                passErr.setVisibility(View.INVISIBLE);
                flag = true;
            }
            hideKeyBoard(getContext(), getActivity());

            //регистрация пользователя
            if (flag) {
                createAccount(email, passwordd);
            }
        });

        register.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                try {Thread.sleep(800);} catch (InterruptedException e) {}
                FragmentManager fm = getActivity().getSupportFragmentManager();
                FragmentTransaction ft = fm.beginTransaction();
                ft.replace(R.id.homeLayout, new FragmentSignIn());
                ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });

        return rootView;
    }

    private void createAccount(final String email, String password){
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    writeNewUser(userId, username, email, passwordd);
                    register.transitionToEnd();
                    txtErr.setVisibility(View.INVISIBLE);
                } else {
                    Context context = getContext();
                    if (!isOnline(context)){
                        txtErr.setText("Нет подключения к сети");
                    } else {
                        txtErr.setText("Данный адрес почты уже зарегестрирован");
                    }
                    txtErr.setVisibility(View.VISIBLE);
                }
            }
        });
    }
}