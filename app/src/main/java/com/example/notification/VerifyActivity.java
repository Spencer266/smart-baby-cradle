package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.TextView;

public class VerifyActivity extends AppCompatActivity {

    private TextView editText;
    private EditText verificationCode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("VerifyActivity", "Calling onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
    }

    private void initUI() {
        editText = findViewById()
    }
}