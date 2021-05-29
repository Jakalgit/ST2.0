package com.example.soundtranslator.fragment;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.soundtranslator.R;

import static com.example.soundtranslator.fragment.FragmentHome.pHistory;

public class FragmentHistory extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    private TextView txtHis1, txtHis2, txtHis3, txtHis4, txtHis5;

    private ImageView btnBack;

    public FragmentHistory() {
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
        final View historyView = inflater.inflate(R.layout.fragment_history, container, false);

        txtHis1 = historyView.findViewById(R.id.txtHis1);
        txtHis2 = historyView.findViewById(R.id.txtHis2);
        txtHis3 = historyView.findViewById(R.id.txtHis3);
        txtHis4 = historyView.findViewById(R.id.txtHis4);
        txtHis5 = historyView.findViewById(R.id.txtHis5);

        btnBack = historyView.findViewById(R.id.btnBack3);

        viewHistory();

        isEmpty(txtHis1);
        isEmpty(txtHis2);
        isEmpty(txtHis3);
        isEmpty(txtHis4);
        isEmpty(txtHis5);

        btnBack.setOnClickListener(v -> {
            FragmentManager fm = getActivity().getSupportFragmentManager();
            FragmentTransaction ft = fm.beginTransaction();
            ft.replace(R.id.homeLayout, new FragmentHome());
            ft.setTransition(FragmentTransaction.TRANSIT_FRAGMENT_OPEN);
            ft.commit();
        });

        return historyView;
    }

    private void viewHistory(){
        txtHis1.setText(pHistory.his1);
        txtHis2.setText(pHistory.his2);
        txtHis3.setText(pHistory.his3);
        txtHis4.setText(pHistory.his4);
        txtHis5.setText(pHistory.his5);
    }

    private void isEmpty(TextView txt){
        String t = txt.getText().toString();
        if (t.isEmpty()){
            txt.setVisibility(View.INVISIBLE);
        }
    }
}