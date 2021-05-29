package com.example.soundtranslator.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.soundtranslator.R;

import java.util.Locale;

import static com.example.soundtranslator.fragment.FragmentHome.LanguageTTsFrom;
import static com.example.soundtranslator.fragment.FragmentHome.LanguageTTsTo;
import static com.example.soundtranslator.fragment.FragmentHome.TextFrom;
import static com.example.soundtranslator.fragment.FragmentHome.TextTo;
import static com.example.soundtranslator.fragment.FragmentHome.flagLang;
import static com.example.soundtranslator.fragment.FragmentHome.langFrom;
import static com.example.soundtranslator.fragment.FragmentHome.langTo;

public class FragmentLangFrom extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private Button btnChina, btnRussian, btnEnglish, btnJapan, btnFrance, btnSpanish;

    public FragmentLangFrom() {
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
        final View langView = inflater.inflate(R.layout.fragment_lang_from, container, false);

        btnChina = langView.findViewById(R.id.btnChina);
        btnRussian = langView.findViewById(R.id.btnRussian);
        btnEnglish = langView.findViewById(R.id.btnEnglish);
        btnJapan = langView.findViewById(R.id.btnJapan);
        btnFrance = langView.findViewById(R.id.btnFrance);
        btnSpanish = langView.findViewById(R.id.btnSpanish);

        btnChina.setOnClickListener(v -> {
            if (flagLang){
                langFrom = "zh";
                TextFrom = "Китайский";
                LanguageTTsFrom = Locale.CHINA;
            } else {
                langTo = "zh";
                TextTo = "Китайский";
                LanguageTTsTo = Locale.CHINA;
            }
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentHome());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        btnRussian.setOnClickListener(v -> {
            if(flagLang){
                langFrom = "ru";
                TextFrom = "Русский";
                LanguageTTsFrom = Locale.ENGLISH;
            } else {
                langTo = "ru";
                TextTo = "Русский";
                LanguageTTsTo = Locale.ENGLISH;
            }
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentHome());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        btnEnglish.setOnClickListener(v -> {
            if(flagLang){
                langFrom = "en";
                TextFrom = "Английский";
               LanguageTTsFrom = Locale.ENGLISH;
            } else {
                langTo = "en";
                TextTo = "Английский";
                LanguageTTsTo = Locale.ENGLISH;
            }
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentHome());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        btnJapan.setOnClickListener(v -> {
            if(flagLang){
                langFrom = "ja";
                TextFrom = "Японский";
                LanguageTTsFrom = Locale.JAPAN;
            } else {
                langTo = "ja";
                TextTo = "Японский";
                LanguageTTsTo = Locale.JAPAN;
            }
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentHome());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        btnFrance.setOnClickListener(v -> {
            if(flagLang){
                langFrom = "fr";
                TextFrom = "Французский";
                LanguageTTsFrom = Locale.FRANCE;
            } else {
                langTo = "fr";
                TextTo = "Французский";
                LanguageTTsTo = Locale.FRANCE;
            }
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentHome());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        btnSpanish.setOnClickListener(v -> {
            if(flagLang){
                langFrom = "es";
                TextFrom = "Испанский";
                LanguageTTsFrom = Locale.FRANCE;
            } else {
                langTo = "es";
                TextTo = "Испанкий";
                LanguageTTsTo = Locale.FRANCE;
            }
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentHome());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        return langView;
    }
}
