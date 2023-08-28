package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editEmail;
    private Button resetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ForgotPasswordActivity", "Calling onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        initUI();
        onClickResetButton();
    }

    private void onClickResetButton() {
        Log.i("ForgotPasswordActivity", "Calling onClickResetButton");

//        resetButton.setOnClickListener(view -> {
//            Toast.makeText(this, "This function has not been implemented yet!", Toast.LENGTH_LONG).show();
//        });
    }

    private void initUI() {
        Log.i("ForgotPasswordActivity", "Calling InitUI");

        editEmail = findViewById(R.id.ForgotPassword_editEmail);
        resetButton = findViewById(R.id.ForgotPassword_resetButton);
    }
}