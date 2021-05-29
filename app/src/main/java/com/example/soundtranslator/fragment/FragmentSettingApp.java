package com.example.soundtranslator.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.example.soundtranslator.R;

import static com.example.soundtranslator.fragment.FragmentHome.progressBarSound;
import static com.example.soundtranslator.fragment.FragmentHome.progressBarSpeed;

public class FragmentSettingApp extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private SeekBar skbSound, skbSpeed;

    private Button btnUpSound, btnDownSound, btnUpSpeed, btnDownSpeed;

    private ImageView btnBack;

    private TextView txtProgress1, txtProgress2;

    private int DELTA = 5;

    private SharedPreferences settings;

    public FragmentSettingApp() {
        // Required empty public constructor
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
        // Inflate the layout for this fragment
        final View appView = inflater.inflate(R.layout.fragment_setting_app, container, false);

        skbSound = appView.findViewById(R.id.skbSound);
        skbSpeed = appView.findViewById(R.id.skbSpeed);

        btnUpSound = appView.findViewById(R.id.btnUpSound);
        btnDownSound = appView.findViewById(R.id.btnDownSound);
        btnUpSpeed = appView.findViewById(R.id.btnUpSpeed);
        btnDownSpeed = appView.findViewById(R.id.btnDownSpeed);
        btnBack = appView.findViewById(R.id.btnBack4);

        settings = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);

        txtProgress1 = appView.findViewById(R.id.txtProgress1);
        txtProgress2 = appView.findViewById(R.id.txtProgress2);

        btnUpSound.setOnClickListener(v -> {
            int progress = skbSound.getProgress();
            if(progress + DELTA > skbSound.getMax()){
                skbSound.setProgress(0);
            } else {
                skbSound.setProgress(progress + DELTA);
            }
            progressBarSound = progress;
            txtProgress1.setText(skbSound.getProgress() + "/" + skbSound.getMax());
        });

        btnDownSound.setOnClickListener(v -> {
            int progress = skbSound.getProgress();
            if(progress - DELTA < 5){
                skbSound.setProgress(0);
            } else {
                skbSound.setProgress(progress - DELTA);
            }
            progressBarSound = progress;
            txtProgress1.setText(skbSound.getProgress() + "/" + skbSound.getMax());
        });

        btnUpSpeed.setOnClickListener(v -> {
            int progress = skbSpeed.getProgress();
            if(progress + DELTA > skbSpeed.getMax()){
                skbSpeed.setProgress(0);
            } else {
                skbSpeed.setProgress(progress + DELTA);
            }
            progressBarSpeed = progress;
            txtProgress2.setText(skbSpeed.getProgress() + "/" + skbSpeed.getMax());
        });

        btnDownSpeed.setOnClickListener(v -> {
            int progress = skbSpeed.getProgress();
            if(progress - DELTA < 5){
                skbSpeed.setProgress(0);
            } else {
                skbSpeed.setProgress(progress - DELTA);
            }
            progressBarSpeed = progress;
            txtProgress2.setText(skbSpeed.getProgress() + "/" + skbSpeed.getMax());
        });

        btnBack.setOnClickListener(v ->{
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentSetting());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_FADE);
            ft.commit();
        });

        skbSpeed.setMax(100);
        skbSpeed.setProgress(progressBarSpeed);

        skbSound.setMax(100);
        skbSound.setProgress(progressBarSound);

        txtProgress1.setText(skbSound.getProgress() + "/" + skbSound.getMax());
        txtProgress2.setText(skbSpeed.getProgress() + "/" + skbSpeed.getMax());

        skbSound.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtProgress1.setText(skbSound.getProgress() + "/" + skbSound.getMax());

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressBarSound = skbSound.getProgress();
                saveProgressSound();
            }
        });

        skbSpeed.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                txtProgress2.setText(skbSpeed.getProgress() + "/" + skbSpeed.getMax());
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                progressBarSpeed = skbSpeed.getProgress();
                saveProgressSpeed();
            }
        });

        return appView;
    }

    private void saveProgressSound(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Volume", progressBarSound);
        editor.apply();
    }

    private void saveProgressSpeed(){
        SharedPreferences.Editor editor = settings.edit();
        editor.putInt("Speed", progressBarSpeed);
        editor.apply();
    }
}