package com.example.notification.fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.amplifyframework.core.Amplify;
import com.example.notification.R;

public class Home extends Fragment {
    private View View;
    private Spinner deviceChooser;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View = inflater.inflate(R.layout.fragment_home, container, false);

        init();
        initUI();

        return View;
    }

    private void init() {

    }

    private void initUI() {
        deviceChooser = View.findViewById(R.id.Home_deviceChooser);

    }

}
