package com.example.notification.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.BabyData;
import com.amplifyframework.datastore.generated.model.Bracelet;
import com.amplifyframework.datastore.generated.model.Controller;
import com.amplifyframework.datastore.generated.model.Cradle;
import com.example.notification.R;


public class History extends Fragment {
    private View view;
    private TextView deviceID, timestamp;
    private TextView heartBeats, oxygen, temperature;
    private TextView environmentTemp, humidity, cry;
    private TextView fanSpeed, swaying, acTemp;
    private CheckBox playingMusic, humidifier;
    private Button pushButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history, container, false);
        initUI();
        onClickPushButton();

        return view;
    }

    private void onClickPushButton() {
        Log.i("History", "Calling onClickPushButton");

        pushButton.setOnClickListener(view -> {
            Bracelet bracelet = Bracelet.builder()
                    .heartBeats(Double.parseDouble("0" + heartBeats.getText().toString()))
                    .oxygen(Double.parseDouble("0" + oxygen.getText().toString()))
                    .temperature(Double.parseDouble("0" + temperature.getText().toString()))
                    .build();
            Cradle cradle = Cradle.builder()
                    .environmentTemp(Double.parseDouble("0" + environmentTemp.getText().toString()))
                    .humidity(Double.parseDouble("0" + humidity.getText().toString()))
                    .cry(cry.getText().toString())
                    .build();
            Controller controller = Controller.builder()
                    .fanSpeed(fanSpeed.getText().toString())
                    .swaying(swaying.getText().toString())
                    .playingMusic(playingMusic.isActivated())
                    .humidifier(humidifier.isActivated())
                    .acTemp(Double.parseDouble("0" + acTemp.getText().toString()))
                    .build();
            BabyData babyData = BabyData.builder()
                    .deviceId(deviceID.getText().toString())
                    .timestamp(Integer.parseInt("0" + timestamp.getText().toString()))
                    .bracelet(bracelet)
                    .cradle(cradle)
                    .controller(controller)
                    .build();

            Amplify.API.mutate(
                    ModelMutation.create(babyData),
                    response -> {
                        Log.i("API", "Successfully pushed baby data to cloud with id: " + response);
                        ToastMessage.showToastMessage(
                                getActivity(),
                                "Successfully pushed baby data to cloud!",
                                "onClickPushButton"
                        );
                    },
                    error -> {
                        Log.e("API", "Failed to pushed baby data to cloud", error);
                        ToastMessage.showToastMessage(
                                getActivity(),
                                "Something went wrong when pushing data to cloud",
                                "onClickPushButton"
                        );
                    }
            );
        });
    }

    private void initUI() {
        deviceID = view.findViewById(R.id.History_deviceID);
        timestamp = view.findViewById(R.id.History_timestamp);
        heartBeats = view.findViewById(R.id.History_heartBeats);
        oxygen = view.findViewById(R.id.History_oxygen);
        temperature = view.findViewById(R.id.History_temperature);
        environmentTemp = view.findViewById(R.id.History_environmentTemp);
        humidity = view.findViewById(R.id.History_humidity);
        cry = view.findViewById(R.id.History_cry);
        fanSpeed = view.findViewById(R.id.History_fanSpeed);
        swaying = view.findViewById(R.id.History_swaying);
        playingMusic = view.findViewById(R.id.History_playingMusic);
        humidifier = view.findViewById(R.id.History_humidifier);
        acTemp = view.findViewById(R.id.History_acTemp);
        pushButton = view.findViewById(R.id.History_pushButton);
    }
}
