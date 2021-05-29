package com.example.soundtranslator.fragment;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.motion.widget.MotionLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.soundtranslator.R;
import com.example.soundtranslator.User;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import static com.example.soundtranslator.Methods.hideKeyBoard;
import static com.example.soundtranslator.Methods.isOnline;

public class FragmentChangeAccount extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView btnBack, chgEmail, chgUsername, chgPassword;

    private Button btnExist;

    private EditText edtEmail, edtUsername, edtPassword;

    private TextView txtEmail, txtUsername, txtPassword, txtError;

    private DatabaseReference mDatabase;

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
        final View changeView = inflater.inflate(R.layout.fragment_change_account, container, false);

        btnBack = changeView.findViewById(R.id.btnBack);
        btnExist = changeView.findViewById(R.id.btnExist);

        txtEmail = changeView.findViewById(R.id.txtEmail);
        txtUsername = changeView.findViewById(R.id.txtUsername);
        txtPassword = changeView.findViewById(R.id.txtPassword);
        txtError = changeView.findViewById(R.id.txtError1);

        chgEmail = changeView.findViewById(R.id.chgEmail);
        chgUsername = changeView.findViewById(R.id.chgUsername);
        chgPassword = changeView.findViewById(R.id.chgPassowrd);
        final Animation animation1 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_btn_swap);
        animation1.setFillAfter(true);
        final Animation animation2 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_btn_swap);
        animation2.setFillAfter(true);
        final Animation animation3 = AnimationUtils.loadAnimation(getContext(), R.anim.anim_btn_swap);
        animation3.setFillAfter(true);

        edtEmail = changeView.findViewById(R.id.edtEmail);
        edtUsername = changeView.findViewById(R.id.edtUsername);
        edtPassword = changeView.findViewById(R.id.edtPassword);

        mDatabase = FirebaseDatabase.getInstance().getReference();

        String mail = FirebaseAuth.getInstance().getCurrentUser().getEmail();
        viewUser(mail);

        chgEmail.setOnClickListener(v -> {
            Context context = getContext();
            hideKeyBoard(getContext(), getActivity());
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            String emailNew = edtEmail.getText().toString();
            AuthCredential authCredential = EmailAuthProvider.getCredential(txtEmail.getText().toString(), txtPassword.getText().toString());
            if (!emailNew.isEmpty()) {
                currentUser.reauthenticate(authCredential).addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        currentUser.updateEmail(emailNew).addOnCompleteListener(task1 -> {
                            if (task1.isSuccessful()) {
                                mDatabase.child("Users").child(currentUser.getUid()).child("email").setValue(emailNew);
                                txtEmail.setText(emailNew);
                                txtError.setText("");
                            } else {
                                if (isOnline(context) && (emailNew.contains("@"))) {
                                    txtError.setText("Данный адрес почты уже зарегестрирован");
                                } else if(emailNew.contains("@")) {
                                    txtError.setText("Нет подключение к сети");
                                } else {
                                    txtError.setText("Неверный формат");
                                }
                            }
                        });
                    } else {
                        if (!isOnline(context)) {
                            txtError.setText("Нет подключение к сети");
                        }
                    }
                });
            } else {
                txtError.setText("Адрес почты не может быть пустым");
            }
            chgEmail.startAnimation(animation1);
            viewUser(currentUser.getUid());
        });

        chgUsername.setOnClickListener(v -> {
            hideKeyBoard(getContext(), getActivity());
            String usernameNew = edtUsername.getText().toString();
            if(isOnline(getContext())) {
                if (usernameNew.length() >= 2) {
                    FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                    mDatabase.child("Users").child(currentUser.getUid()).child("username").setValue(usernameNew);
                    txtUsername.setText(usernameNew);
                } else {
                    txtError.setText("Имя пользователя от 2 символов");
                }
                FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
                chgUsername.startAnimation(animation2);
                viewUser(currentUser.getUid());
            } else {
                txtError.setText("Нет подклбчения к сети");
            }
        });

        chgPassword.setOnClickListener(v -> {
            hideKeyBoard(getContext(), getActivity());
            String passwordNew = edtPassword.getText().toString();
            Context context = getContext();
            FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
            if(passwordNew.length() >= 6) {
                AuthCredential authCredential = EmailAuthProvider.getCredential(txtEmail.getText().toString(), txtPassword.getText().toString());
                currentUser.reauthenticate(authCredential).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            currentUser.updatePassword(passwordNew).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        mDatabase.child("Users").child(currentUser.getUid()).child("password").setValue(passwordNew);
                                        txtPassword.setText(passwordNew);
                                        txtError.setText("");
                                    } else if (!isOnline(context)) {
                                        txtError.setText("Нет подключение к сети");
                                    }
                                }
                            });
                        } else {
                            if (isOnline(context)) {
                                txtError.setText("Нет подключение к сети");
                            }
                        }
                    }
                });
            } else {
                txtError.setText("Пароль от 6 символов");
            }
            viewUser(currentUser.getUid());
            chgPassword.startAnimation(animation3);
        });

        btnBack.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentSetting());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        btnExist.setOnClickListener(v -> {
            FirebaseAuth.getInstance().signOut();
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentSignIn());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });
        return changeView;
    }

    private void viewUser(String email){
        DatabaseReference myRef = mDatabase.child("Users");
        Query query = myRef.orderByChild("email").equalTo(email);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    User user = dataSnapshot.getValue(User.class);
                    txtEmail.setText(user.email);
                    txtUsername.setText(user.username);
                    txtPassword.setText(user.password);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }
}