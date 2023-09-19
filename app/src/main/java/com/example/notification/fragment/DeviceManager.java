package com.example.notification.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.api.graphql.model.ModelQuery;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.core.model.query.predicate.QueryPredicate;
import com.amplifyframework.core.model.query.predicate.QueryPredicates;
import com.amplifyframework.datastore.generated.model.UserDevice;
import com.example.notification.R;

public class DeviceManager extends Fragment {
    private View view;
    private EditText deviceID, mappingName;
    private Button connectButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_device_manager, container, false);

        initUI();
        onClickConnectButton();

        return view;
    }

    private void onClickConnectButton() {
        Log.i("DeviceManager", "Calling onClickConnectButton");


        connectButton.setOnClickListener(view -> {
            UserDevice userDevice = UserDevice.builder()
                    .userId(getUserId())
                    .deviceId(getDeviceId())
                    .mappingName(getMappingName())
                    .build();

            Amplify.API.query(
                    ModelQuery.list(
                            UserDevice.class,
                            UserDevice.USER_ID.eq(getUserId())
                                    .and(UserDevice.DEVICE_ID.eq(getUserId()))
                    ),
                    response -> {
                        boolean hasEntries = false;
                        for (UserDevice userDevice1 : response.getData()) {
                            hasEntries = true;
                            Log.i("ConnectButton", userDevice1.toString());
                            break;
                        }
                        if (hasEntries) {
                            try {
                                getActivity().runOnUiThread(() -> Toast.makeText(
                                        getActivity(),
                                        "You have already connected to this device",
                                        Toast.LENGTH_SHORT).show()
                                );
                            } catch (NullPointerException e) {
                                Log.e("AddingDevice", "Can not show toast message", e);
                            }
                        } else {
                            addDevice(userDevice);
                        }
                    },
                    error -> {
                        Log.e("AddingDevice", "Something went wrong", error);
                    }
            );
        });
    }

    public void addDevice(UserDevice userDevice) {
        Amplify.API.mutate(
                ModelMutation.create(userDevice),
                response -> {
                    Log.i("AddingDevice", "Successfully connect to device with id: " + response.getData().getId());
                    ToastMessage.showToastMessage(
                            getActivity(),
                            "Successfully connected to device: " + response.getData().getDeviceId(),
                            "addDevice"
                    );
                },
                error -> {
                    ToastMessage.showToastMessage(
                            getActivity(),
                            error.getCause().toString(),
                            "addDevice"
                    );
                }
        );
    }

    public String getDeviceId() {
        return deviceID.getText().toString();
    }

    public String getMappingName() {
        return mappingName.getText().toString();
    }

    public String getUserId() {
        return getArguments().getString("userId");
    }

    private void initUI() {
        Log.i("DeviceManager", "Calling initUI");

        deviceID      = view.findViewById(R.id.DeviceManager_deviceID);
        connectButton = view.findViewById(R.id.DeviceManager_connectButton);
        mappingName   = view.findViewById(R.id.DeviceManager_mappingName);
    }
}