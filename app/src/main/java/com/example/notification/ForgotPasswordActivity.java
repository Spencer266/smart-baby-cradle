package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

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
        resetButton.setOnClickListener(view -> {
            String userEmail = editEmail.getText().toString().trim();

            Amplify.Auth.resetPassword(
                    userEmail,
                    result -> Log.i("ResetPassword", result.toString()),
                    error -> Log.e("ResetPassword", error.toString())
            );
        });
    }

    private void initUI() {
        Log.i("ForgotPasswordActivity", "Calling InitUI");

        editEmail = findViewById(R.id.ForgotPassword_editEmail);
        resetButton = findViewById(R.id.ForgotPassword_resetButton);
    }
}