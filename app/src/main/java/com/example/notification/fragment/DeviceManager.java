package com.example.notification.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.generated.model.UserDevice;
import com.example.notification.R;

public class DeviceManager extends Fragment {
    private View view;
    private EditText deviceID;
    private Button connectButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_manager, container, false);

        initUI();

        return view;
    }

    private void onClickConnectButton() {
        Log.i("DeviceManager", "Calling onClickConnectButton");


        connectButton.setOnClickListener(view -> {
//            UserDevice userDevice = UserDevice.builder()
//                    .userId()
//                    .deviceId()
//                    .build();
//            Amplify.API.mutate(
//
//            );
        });
    }

    private void initUI() {
        Log.i("DeviceManager", "Calling initUI");

        deviceID      = view.findViewById(R.id.deviceManager_deviceID);
        connectButton = view.findViewById(R.id.deviceManager_connectButton);
    }
}