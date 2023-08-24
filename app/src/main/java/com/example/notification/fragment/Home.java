package com.example.notification.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.amplifyframework.core.Amplify;
import com.example.notification.DataAdapter;
import com.example.notification.DataItem;
import com.example.notification.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Home extends Fragment {
    private View mView;
    private Button btn_update;
    private TextView temperature, humidity, heart_beat;
    private Button btn_show, btn_test;
    private String userID;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mView = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        btn_show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getActivity(), "success", Toast.LENGTH_SHORT);
//                showUI2();
            }
        });

        return mView;
    }

    private void showUI() {
//        CollectionReference userCollectionRef = fStore.collection("users");
//
//        userCollectionRef.document(userID).collection("data")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getActivity(), "Get date success", Toast.LENGTH_SHORT).show();
//                            List<DataItem> dataItems = new ArrayList<>();
//                            for (QueryDocumentSnapshot dateDocument : task.getResult()) {
//                                Toast.makeText(getActivity(), "???", Toast.LENGTH_SHORT).show();
////                                String date = dateDocument.getId();
////                                Toast.makeText(getActivity(), "???", Toast.LENGTH_SHORT).show();
////                                CollectionReference timeDataCollectionRef = dateDocument.getReference().collection("time_data");
////                                timeDataCollectionRef.get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
////                                    @Override
////                                    public void onComplete(@NonNull Task<QuerySnapshot> timeDataTask) {
////                                        if (timeDataTask.isSuccessful()) {
////                                            Toast.makeText(getActivity(), "Get time data success", Toast.LENGTH_SHORT).show();
////                                            for (QueryDocumentSnapshot timeDocument : timeDataTask.getResult()) {
////                                                String time = timeDocument.getId();
////                                                double temperatureValue = timeDocument.getDouble("temperature");
////                                                double humidityValue = timeDocument.getDouble("humidity");
////                                                double heartBeatValue = timeDocument.getDouble("heart_beat");
////                                                dataItems.add(new DataItem(temperatureValue, humidityValue, heartBeatValue, date, time));
////                                            }
////
////                                            // Sử dụng RecyclerView để hiển thị danh sách dữ liệu
////                                            RecyclerView recyclerView = mView.findViewById(R.id.recycler_view);
////                                            DataAdapter adapter = new DataAdapter(dataItems);
////                                            recyclerView.setAdapter(adapter);
////                                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
////
////                                            // Thêm đường chia giữa các items
////                                            DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(recyclerView.getContext(), LinearLayoutManager.VERTICAL);
////                                            recyclerView.addItemDecoration(dividerItemDecoration);
////                                        } else {
////                                            Toast.makeText(getActivity(), "Get time data fail", Toast.LENGTH_SHORT).show();
////                                        }
////                                    }
////                                });
//                            }
//                            Toast.makeText(getActivity(), "!!!!", Toast.LENGTH_SHORT).show();
//                        } else {
//                            Toast.makeText(getActivity(), "Get data fail", Toast.LENGTH_SHORT).show();
//                        }
//                    }
//                });
    }


    private void showUI2() {
//        CollectionReference userCollectionRef = fStore.collection("user");
//
//        userCollectionRef.document(userID).collection("data")
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//                            Toast.makeText(getActivity(), "get data success", Toast.LENGTH_SHORT);
//                            List<DataItem> dataItems = new ArrayList<>();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//                                double temperatureValue = document.getDouble("temperature");
//                                double humidityValue = document.getDouble("humidity");
//                                double heartBeatValue = document.getDouble("heart_beat");
//
//                                dataItems.add(new DataItem(temperatureValue, humidityValue, heartBeatValue));
//                            }
//
//                            // Sử dụng RecyclerView để hiển thị danh sách dữ liệu
//                            RecyclerView recyclerView = mView.findViewById(R.id.recycler_view);
//                            DataAdapter adapter = new DataAdapter(dataItems);
//                            recyclerView.setAdapter(adapter);
//                            recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
//                        } else {
//                            Toast.makeText(getActivity(), "get data fail", Toast.LENGTH_SHORT);
//                        }
//                    }
//                });
    }


    private void init() {
//        btn_update = mView.findViewById(R.id.btn_push);
//        temperature = mView.findViewById(R.id.show_temperature);
//        humidity = mView.findViewById(R.id.show_humidity);
//        heart_beat = mView.findViewById(R.id.show_heart_beat);

        Amplify.Auth.fetchUserAttributes(
            attributes -> {
                Log.i("AuthFetch", "User attributes = " + attributes.toString());
            },
                error -> Log.e("AuthFetch", "Failed to retrieve current user data!")
        );
        btn_show = mView.findViewById(R.id.btn_show);
    }

}
