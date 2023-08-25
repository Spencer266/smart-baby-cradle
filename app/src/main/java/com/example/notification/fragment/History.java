package com.example.notification.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.notification.R;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class History extends Fragment {
    private View mView;
    private EditText temperature, humidity, heart_beat;
    private Button btn_push, btn_push2;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);
        init();

        return mView;
    }

    private void init() {
        temperature = mView.findViewById(R.id.History_itemHeartBeat);
        humidity = mView.findViewById(R.id.History_itemHumidity);
        heart_beat = mView.findViewById(R.id.History_itemHeartBeat);
        btn_push = mView.findViewById(R.id.History_pushButton1);
        btn_push2 = mView.findViewById(R.id.History_pushButton2);
    }
}
