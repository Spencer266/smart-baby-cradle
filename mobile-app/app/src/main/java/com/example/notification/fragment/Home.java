package com.example.notification.fragment;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amplifyframework.api.ApiOperation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.api.graphql.model.ModelSubscription;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.BabyData;
import com.amplifyframework.datastore.generated.model.UserDevice;
import com.example.notification.R;

import java.util.ArrayList;
import java.util.Locale;

public class Home extends Fragment {
    private View View;
    private ArrayList <String> deviceNameList, deviceIdList;
    private Spinner deviceChooser;
    private String currentDevice;
    private LinearLayout warningBackground;
    private ImageView statusIcon;
    private TextView message;
    private TextView heartBeat, oxygenLevel, bodyTemperature;
    private TextView temperature, humidity, cry;
    private TextView fanSpeed, swaying, music, humidifier, acTemperature;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View = inflater.inflate(R.layout.fragment_home, container, false);

        initUI();
        onClickDeviceChooser();
        beginSubscription();

        return View;
    }

    private void beginSubscription() {
         Amplify.API.subscribe(
                ModelSubscription.onCreate(BabyData.class),
                onEstablished -> Log.i("beginSubscription", "Subscription established"),
                onCreated -> {
                    showDeviceData();
                },
                onFailure -> Log.e("beginSubscription", "Subscription failed", onFailure),
                () -> Log.i("beginSubscription", "Subscription completed")
         );

    }

    private void onClickDeviceChooser() {
        Log.i("Home", "Calling onClickDeviceChooser");

        deviceNameList = new ArrayList<>();
        deviceIdList = new ArrayList<>();

        deviceNameList.add("");
        deviceIdList.add("");

        Log.i("Home", "Current userID: " + getUserId());

        Amplify.API.query(
                ModelQuery.list(
                        UserDevice.class,
                        UserDevice.USER_ID.eq(getUserId())
                ),
                response -> {
                    for (UserDevice userDevice : response.getData()) {
                        if (!userDevice.getUserId().equals(getUserId())) {
                            Log.i("FetchData", "Something is not right with the query!");
                        }
                        deviceIdList.add(userDevice.getDeviceId());
                        deviceNameList.add(userDevice.getMappingName());
                    }
                },
                error -> Log.e("FetchData", "Can not get data from UserDevice table!")
        );

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), R.layout.device_chooser_layout, deviceNameList);
        deviceChooser.setAdapter(adapter);
        deviceChooser.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.i("Selecting", (String) parent.getItemAtPosition(position));

                for (int i = 0; i < deviceNameList.size(); i++) {
                    if (deviceNameList.get(i).equals((String) parent.getItemAtPosition(position))) {
                        currentDevice = deviceIdList.get(i);
                    }
                }
                showDeviceData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });
    }

    public void showDeviceData() {
        Log.i("Home", "Calling showDeviceData");
        Amplify.API.query(
                ModelQuery.list(
                    BabyData.class,
                    BabyData.DEVICE_ID.eq(currentDevice)
                ),
                response -> {
                    Log.i("showDeviceData", "Current device: " + currentDevice);
                    if (response.hasData()) {
                        Log.i("showDeviceData", "Have some data");
                        int timeStamp = -1;
                        for (BabyData babyData : response.getData()) {
                            timeStamp = Math.max(timeStamp, babyData.getTimestamp());
                        }
                        for (BabyData babyData : response.getData()) {
                            if (timeStamp == babyData.getTimestamp()) {
                                displayBabyData(babyData);
                            }
                        }
                        if (timeStamp == -1) {
                            displayDefaultBabyData();
                        }
                    }
                },
                error -> Log.i("showDeviceData", "Something went wrong", error)
        );
    }

    private void displayStatus(int id, String color, String currentMessage) {
        statusIcon.setImageResource(id);
        warningBackground.setBackgroundColor(Color.parseColor(color));
        message.setText(currentMessage);
    }

    private void displayDefaultBabyData() {
        Log.i("Home", "Calling displayDefaultBabyData");
        getActivity().runOnUiThread(() -> {
            heartBeat.setText("No data");
            oxygenLevel.setText("No data");
            bodyTemperature.setText("No data");
            temperature.setText("No data");
            humidity.setText("No data");
            cry.setText("No data");
            fanSpeed.setText("No data");
            swaying.setText("No data");
            music.setText("No data");
            humidifier.setText("No data");
            acTemperature.setText("No data");
            displayStatus(R.drawable.ic_ok_sign, "#ffff00", "Nothing to show!");
        });
    }

    private void handleMessage(BabyData babyData) {
        Log.i("Home", "Calling handleMessage");
        ArrayList<String> warning = new ArrayList<>();

        if (babyData.getBracelet().getTemperature() <= 36) {
            warning.add("Low body temperature");
        } else if (babyData.getBracelet().getTemperature() >= 37.5) {
            warning.add("High body temperature");
        }

        if (babyData.getBracelet().getHeartBeats() > 160) {
            warning.add("High heart beats");
        } else if (babyData.getBracelet().getHeartBeats() >= 70 && babyData.getBracelet().getHeartBeats() < 120) {
            warning.add("Low heart beats");
        }

        if (babyData.getBracelet().getOxygen() < 90) {
            warning.add("Low oxygen saturation");
        }

        if (warning.size() == 0) {
            displayStatus(R.drawable.ic_ok_sign, "#00ff00", "Baby is good!");
        } else {
            String warningMessage = "Something is wrong with baby!";
            for (int i = 0; i < warning.size(); i++) {
                warningMessage += "\n";
                warningMessage += warning.get(i);
            }
            displayStatus(R.drawable.ic_tention_sign, "#ff0000", warningMessage);
        }
    }

    private void displayBabyData(BabyData babyData) {
        Log.i("Home", "Calling displayBabyData");

        try {
            getActivity().runOnUiThread(() -> {
                heartBeat.setText(String.format(Locale.getDefault(), "%.2f", babyData.getBracelet().getHeartBeats()));
                oxygenLevel.setText(String.format(Locale.getDefault(), "%.2f", babyData.getBracelet().getOxygen()));
                bodyTemperature.setText(String.format(Locale.getDefault(), "%.2f", babyData.getBracelet().getTemperature()));
                temperature.setText(String.format(Locale.getDefault(), "%.2f", babyData.getCradle().getEnvironmentTemp()));
                humidity.setText(String.format(Locale.getDefault(), "%.2f", babyData.getCradle().getHumidity()));
                cry.setText(babyData.getCradle().getCry());
                handleMessage(babyData);
            });
        } catch (NullPointerException e) {
            Log.e("Home", "Failed to display baby data!", e);
        }
    }

    public String getUserId() {
        return getArguments().getString("userId");
    }

    private void initUI() {
        deviceChooser     = View.findViewById(R.id.Home_deviceChooser);
        heartBeat         = View.findViewById(R.id.Home_heartRate);
        oxygenLevel       = View.findViewById(R.id.Home_oxygenLevel);
        bodyTemperature   = View.findViewById(R.id.Home_bodyTemperature);
        temperature       = View.findViewById(R.id.Home_temperature);
        humidity          = View.findViewById(R.id.Home_humidity);
        cry               = View.findViewById(R.id.Home_cry);
        fanSpeed          = View.findViewById(R.id.Home_fanSpeed);
        swaying           = View.findViewById(R.id.Home_swaying);
        music             = View.findViewById(R.id.Home_music);
        humidifier        = View.findViewById(R.id.Home_humidifier);
        acTemperature     = View.findViewById(R.id.Home_acTemperature);
        warningBackground = View.findViewById(R.id.Home_warningBackground);
        statusIcon        = View.findViewById(R.id.Home_statusIcon);
        message           = View.findViewById(R.id.Home_message);
    }
}
