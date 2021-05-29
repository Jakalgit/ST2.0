package com.example.soundtranslator.fragment;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.soundtranslator.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

import static com.example.soundtranslator.Methods.hideKeyBoard;
import static com.example.soundtranslator.Methods.isOnline;


public class FragmentSignIn extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private MotionLayout motionLayout;

    private Button signUp, signIn;

    private EditText login, password;

    private TextView error;


    private FirebaseAuth mAuth;

    private String mParam1;
    private String mParam2;

    public FragmentSignIn() {

    }

    public static FragmentSignIn newInstance(String param1, String param2) {
        FragmentSignIn fragment = new FragmentSignIn();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

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
        final View signView = inflater.inflate(R.layout.fragment_sign_in, container, false);

        motionLayout = signView.findViewById(R.id.motion1);

        mAuth = FirebaseAuth.getInstance();

        login = signView.findViewById(R.id.email1);
        password = signView.findViewById(R.id.password);
        error = signView.findViewById(R.id.error);

        signUp = signView.findViewById(R.id.signUp);
        signIn = signView.findViewById(R.id.signIn);


        signIn.setOnClickListener(v -> {
            String email = login.getText().toString();
            String pass = password.getText().toString();
            boolean flag = true;

            if (email.isEmpty() || pass.isEmpty()) {
                error.setText("Поля не могут быть пустыми");
                flag = false;
            } else {
                error.setText("");
                flag = true;
            }

            hideKeyBoard(getContext(), getActivity());

            //аутентификация пользователя
            if (flag) {
                signIn(email, pass);
            }

        });

        signUp.setOnClickListener(v -> {
            hideKeyBoard(getContext(), getActivity());
            FragmentRegister register = new FragmentRegister();
            FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.homeLayout, register).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });
        motionLayout.setTransitionListener(new MotionLayout.TransitionListener() {
            @Override
            public void onTransitionStarted(MotionLayout motionLayout, int i, int i1) {

            }

            @Override
            public void onTransitionChange(MotionLayout motionLayout, int i, int i1, float v) {

            }

            @Override
            public void onTransitionCompleted(MotionLayout motionLayout, int i) {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {

                }
                FragmentHome home = new FragmentHome();
                FragmentTransaction ft = getActivity().getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.homeLayout, home).setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
                ft.commit();
            }

            @Override
            public void onTransitionTrigger(MotionLayout motionLayout, int i, boolean b, float v) {

            }
        });

        return signView;
    }

    private void signIn(String email, String password) {
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(getActivity(), new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    error.setText("");
                    motionLayout.transitionToEnd();
                } else {
                    Context context = getContext();
                    if (!isOnline(context)) {
                        error.setText("Нет подключения к сети");
                    } else {
                        error.setText("Неверный пароль или адрес почты");
                    }
                }
            }
        });
    }
}