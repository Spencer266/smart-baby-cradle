package com.example.notification.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
                        int timeStamp = 0;
                        for (BabyData babyData : response.getData()) {
                            timeStamp = Math.max(timeStamp, babyData.getTimestamp());
                        }
                        for (BabyData babyData : response.getData()) {
                            if (timeStamp == babyData.getTimestamp()) {
                                displayBabyData(babyData);
                            }
                        }
                    }
                },
                error -> Log.i("showDeviceData", "Something went wrong", error)
        );
    }

    public void displayBabyData(BabyData babyData) {
        Log.i("Home", "Calling displayBabyData");

        try {
            getActivity().runOnUiThread(() -> {
                heartBeat.setText(String.format(Locale.getDefault(), "%.2f", babyData.getBracelet().getHeartBeats()));
                oxygenLevel.setText(String.format(Locale.getDefault(), "%.2f", babyData.getBracelet().getOxygen()));
                bodyTemperature.setText(String.format(Locale.getDefault(), "%.2f", babyData.getBracelet().getTemperature()));
                temperature.setText(String.format(Locale.getDefault(), "%.2f", babyData.getCradle().getEnvironmentTemp()));
                humidity.setText(String.format(Locale.getDefault(), "%.2f", babyData.getCradle().getHumidity()));
                cry.setText(babyData.getCradle().getCry());
                fanSpeed.setText(babyData.getController().getFanSpeed());
                swaying.setText(babyData.getController().getSwaying());
                music.setText(babyData.getController().getPlayingMusic().toString());
                humidifier.setText(babyData.getController().getHumidifier().toString());
                acTemperature.setText(String.format(Locale.getDefault(), "%.2f", babyData.getController().getAcTemp()));
            });
        } catch (NullPointerException e) {
            Log.e("Home", "Failed to display baby data!", e);
        }
    }

    public String getUserId() {
        return getArguments().getString("userId");
    }

    private void initUI() {
        deviceChooser   = View.findViewById(R.id.Home_deviceChooser);
        heartBeat       = View.findViewById(R.id.Home_heartRate);
        oxygenLevel     = View.findViewById(R.id.Home_oxygenLevel);
        bodyTemperature = View.findViewById(R.id.Home_bodyTemperature);
        temperature     = View.findViewById(R.id.Home_temperature);
        humidity        = View.findViewById(R.id.Home_humidity);
        cry             = View.findViewById(R.id.Home_cry);
        fanSpeed        = View.findViewById(R.id.Home_fanSpeed);
        swaying         = View.findViewById(R.id.Home_swaying);
        music           = View.findViewById(R.id.Home_music);
        humidifier      = View.findViewById(R.id.Home_humidifier);
        acTemperature   = View.findViewById(R.id.Home_ac_temperature);
    }
}
