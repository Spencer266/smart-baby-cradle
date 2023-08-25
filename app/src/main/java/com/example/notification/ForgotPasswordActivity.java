package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

public class ForgotPasswordActivity extends AppCompatActivity {

    private EditText editEmail;
    private Button resetButton;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ForgotPasswordActivity", "Calling onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);
        uiInit();
        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = editEmail.getText().toString().trim();

                Amplify.Auth.resetPassword(
                        "username",
                        result -> Log.i("ResetPassword", result.toString()),
                        error -> Log.e("ResetPassword", error.toString())
                );
            }
        });
    }

    private void uiInit() {
        Log.i("ForgotPasswordActivity", "Calling uiInit");

        editEmail = findViewById(R.id.ForgotPassword_editEmail);
        resetButton = findViewById(R.id.ForgotPassword_resetButton);
    }
}