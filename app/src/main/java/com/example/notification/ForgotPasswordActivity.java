package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.amplifyframework.core.Amplify;

public class ForgotPasswordActivity extends AppCompatActivity {

    private TextView str_email;
    private Button btn_new_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("ForgotPasswordActivity", "Calling onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_password);
        uiInit();
        btn_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String emailAddress = str_email.getText().toString().trim();

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

        str_email = findViewById(R.id.str_email);
        btn_new_password = findViewById(R.id.btn_new_password);
    }
}