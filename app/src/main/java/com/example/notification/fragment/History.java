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
    private EditText temerature, humidity, heart_beat;
    private Button btn_push, btn_push2;
    private FirebaseAuth fAuth;
    private FirebaseFirestore fStore;
    private String userID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_history, container, false);
        init();
        btn_push.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushData();
            }
        });

        btn_push2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                pushData2();
            }
        });
        return mView;
    }

    private void pushData2() {

            DocumentReference userDocRef = fStore.collection("user").document(userID);

            // Tạo một đối tượng SimpleDateFormat với định dạng mong muốn
            SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy  HH:mm:ss", Locale.getDefault());

            // Tạo một kiểu Timestamp cho mốc thời gian hiện tại
            Date currentTime = new Date();
            String formattedTime = dateFormat.format(currentTime);

            DocumentReference timestampDocRef = userDocRef.collection("data").document(formattedTime);

            Map<String, Object> data = new HashMap<>();
            data.put("temperature", Double.parseDouble(temerature.getText().toString().trim()));
            data.put("humidity", Double.parseDouble(humidity.getText().toString().trim()));
            data.put("heart_beat", Double.parseDouble(heart_beat.getText().toString().trim()));
            data.put("timestamp", formattedTime); // Lưu trường thời gian vào Firestore

            timestampDocRef.set(data).addOnSuccessListener(new OnSuccessListener<Void>() {
                @Override
                public void onSuccess(Void unused) {
                    Toast.makeText(getActivity(), "Data pushed successfully", Toast.LENGTH_SHORT).show();
                }
            });

    }

    private void pushData() {
        DocumentReference userDocRef = fStore.collection("users").document(userID);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault());
        SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss", Locale.getDefault());

        Date currentTime = new Date();
        String formattedDate = dateFormat.format(currentTime);
        String formattedTime = timeFormat.format(currentTime);

        CollectionReference dataCollectionRef = userDocRef.collection("data");
        DocumentReference timeDocRef = dataCollectionRef.document(formattedDate);

        Map<String, Object> data = new HashMap<>();
        data.put("temperature", Double.parseDouble(temerature.getText().toString().trim()));
        data.put("humidity", Double.parseDouble(humidity.getText().toString().trim()));
        data.put("heart_beat", Double.parseDouble(heart_beat.getText().toString().trim()));

        timeDocRef.collection("time_data").document(formattedTime)
                .set(data)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(getActivity(), "Data pushed successfully", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void init() {
        temerature = mView.findViewById(R.id.item_temperature);
        humidity = mView.findViewById(R.id.item_humidity);
        heart_beat = mView.findViewById(R.id.item_heart_beat);
        btn_push = mView.findViewById(R.id.btn_update);
        btn_push2 = mView.findViewById(R.id.btn_update2);
        fAuth = FirebaseAuth.getInstance();
        fStore = FirebaseFirestore.getInstance();
        userID = fAuth.getCurrentUser().getUid();
    }
}
