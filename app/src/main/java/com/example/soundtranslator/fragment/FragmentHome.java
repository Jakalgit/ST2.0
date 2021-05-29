package com.example.soundtranslator.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.speech.tts.TextToSpeech;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soundtranslator.History;
import com.example.soundtranslator.R;
import com.example.soundtranslator.Translate;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;


import org.w3c.dom.Text;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.security.cert.Extension;
import java.util.Locale;

import static com.example.soundtranslator.Methods.hideKeyBoard;
import static com.example.soundtranslator.Methods.isOnline;


public class FragmentHome extends Fragment implements TextToSpeech.OnInitListener {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private ImageView btnSetting, btnHis, btnSpeakLangFrom, btnSpeakLangTo, btnSwap;

    private EditText edtTranslate;

    private TextView txtItog, txtError;

    private Button btnTranslate, btnLangFrom, btnLangTo;

    public static String TextFrom = "Русский", TextTo = "Английский";

    public static String langFrom = "ru", langTo = "en";

    public static boolean flagLang;

    public static History pHistory = new History("", "", "", "", "", "");

    protected static TextToSpeech tts;

    protected static Locale LanguageTTsFrom = Locale.ROOT, LanguageTTsTo = Locale.ENGLISH;

    public static int progressBarSound, progressBarSpeed;

    private String txtHis;

    private SharedPreferences settings;

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
        final View homeView = inflater.inflate(R.layout.fragment_home, container, false);
        txtItog = homeView.findViewById(R.id.translateItog);
        txtError = homeView.findViewById(R.id.txtError);
        edtTranslate = homeView.findViewById(R.id.translate);

        btnLangFrom = homeView.findViewById(R.id.btnLangFrom);
        btnLangTo = homeView.findViewById(R.id.btnLangTo);


        btnSwap = homeView.findViewById(R.id.btnSwap);
        final Animation animation = AnimationUtils.loadAnimation(getContext(), R.anim.anim_btn_swap);
        animation.setFillAfter(true);

        settings = getActivity().getSharedPreferences("Account", Context.MODE_PRIVATE);

        btnSpeakLangFrom = homeView.findViewById(R.id.btnSpeakLangFrom);
        btnSpeakLangTo = homeView.findViewById(R.id.btnSpeakLangTo);

        btnTranslate = homeView.findViewById(R.id.btnTranslate);
        btnSetting = homeView.findViewById(R.id.btnSetting);

        btnHis = homeView.findViewById(R.id.btnHis);

        getHistory();
        getProgressSound();
        getProgressSpeed();

        tts = new TextToSpeech(getContext(), this);
        tts.setPitch((float) progressBarSound/10);
        tts.setSpeechRate((float) progressBarSpeed/20);

        btnLangFrom.setText(TextFrom);
        btnLangTo.setText(TextTo);

        btnSwap.setOnClickListener(v -> {
            btnSwap.startAnimation(animation);
            String sw;
            sw = langFrom;
            langFrom = langTo;
            langTo = sw;

            sw = TextFrom;
            TextFrom = TextTo;
            TextTo = sw;

            btnLangFrom.setText(TextFrom);
            btnLangTo.setText(TextTo);
        });

        btnLangFrom.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentLangFrom());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            flagLang = true;
        });

        btnLangTo.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentLangFrom());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
            flagLang = false;
        });

        btnHis.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentHistory());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        btnTranslate.setOnClickListener(v -> {
            if (!TextTo.equals(TextFrom)) {
                String text = edtTranslate.getText().toString();
                Context context = getContext();
                hideKeyBoard(getContext(), getActivity());
                String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
                if(isOnline(context)) {
                    Thread thread = new Thread(() -> {
                        String textTo = null;
                        try {
                            textTo = Translate.Translate(langFrom, langTo, text);
                            if (textTo.contains("&#39;")){
                                String fileExtension = textTo;
                                String newExtension= fileExtension;
                                newExtension = newExtension.replace("&#39;", "'");
                                textTo = textTo.replace(fileExtension, newExtension);
                            }
                            txtHis = TextFrom + "=>" + TextTo + ": " + text + "=>" + textTo;
                            History historyFin = addHistory(pHistory, txtHis);
                            mDatabase.child("History").child(userId).setValue(historyFin);
                        } catch (IOException e) {}
                        txtItog.setText(textTo);
                        txtError.setText("");
                    });
                    thread.start();
                } else {
                    txtError.setText("Проверьте подключение к сети");
                }
            } else {
                txtError.setText("Выберете другой язык");
            }
        });

        btnSetting.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentSetting());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        btnSpeakLangFrom.setOnClickListener(v -> {
            String text = edtTranslate.getText().toString();
            tts.setLanguage(LanguageTTsFrom);
            speakOut(text);
        });

        btnSpeakLangTo.setOnClickListener(v -> {
            String text = txtItog.getText().toString();
            tts.setLanguage(LanguageTTsTo);
            speakOut(text);
        });


        return homeView;
    }

    private History addHistory(History history, String text){

        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        history.userId = userId;

        if(history.his1.equals("")){
            history.his1 = text;
        } else if (history.his2.equals("")){
            history.his2 = text;
        } else if (history.his3.equals("")){
            history.his3 = text;
        } else if (history.his4.equals("")){
            history.his4 = text;
        } else if (history.his5.equals("")){
            history.his5 = text;
        } else {
            history.his5 = history.his4;
            history.his4 = history.his3;
            history.his3 = history.his2;
            history.his2 = history.his1;
            history.his1 = text;
        }

        return history;
    }

    private void getHistory(){
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference();
        String userId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference myRef = mDatabase.child("History");
        Query query = myRef.orderByChild("userId").equalTo(userId);
        query.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for(DataSnapshot dataSnapshot: snapshot.getChildren()){
                    History history = dataSnapshot.getValue(History.class);
                    pHistory = history;
                }
            }


            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public void onInit(int status) {

    }

    private void speakOut(String text){
        tts.speak(text, TextToSpeech.QUEUE_FLUSH, null, null);
    }

    private void getProgressSound(){
        progressBarSound = settings.getInt("Volume", 50);
    }

    private void getProgressSpeed(){
        progressBarSpeed = settings.getInt("Speed", 50);
    }


}