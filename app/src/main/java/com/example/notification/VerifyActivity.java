package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.core.Amplify;

public class VerifyActivity extends AppCompatActivity {
    private String currentUserEmail;
    private TextView verificationMessage, resendVerificationCodeText;
    private EditText verificationCode;
    private Button confirmButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("VerifyActivity", "Calling onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_verify);
        initUI();
        retrieveUserEmail();
        onClickConfirm();
        onClickResendVerificationCode();
    }

    private void onClickConfirm() {
        Log.i("VerifyActivity", "Calling onClickConfirm");

        confirmButton.setOnClickListener(view -> {
            Amplify.Auth.confirmSignUp(
                    getCurrentUserEmail(),
                    getVerificationCode(),
                    result -> {
                        Log.i("confirmSignUp", "Successfully confirm sign up!" + result);
                        Intent intent = new Intent(this, SignInActivity.class);
                        startActivity(intent);
                        finish();
                    },
                    error -> {
                        Log.e("confirmSignUp", "Unsuccessfully confirm sign up!" + error);
                    }
            );
        });
    }

    private void onClickResendVerificationCode() {
        Log.i("VerifyActivity", "Calling onClickResendVerificationCode");
        resendVerificationCodeText.setOnClickListener(view -> {
            resendVerificationMessage();
        });
    }

    private void retrieveUserEmail() {
        currentUserEmail = getIntent().getStringExtra("userEmail");
        updateVerificationMessage();
    }

    private void updateVerificationMessage() {
        verificationMessage.setText("A verification email have been send to" + currentUserEmail);
        Toast.makeText(this, "Verification code has been resent!", Toast.LENGTH_SHORT).show();
    }
    private void resendVerificationMessage() {
        Amplify.Auth.resendUserAttributeConfirmationCode(AuthUserAttributeKey.email(),
                result -> Log.i("ResendVerificationCode", "Successfully resend verification code: " + result),
                error -> Log.e("ResendVerificationCode", "Failed to send verification code." + error, error)
        );

    }
    public String getCurrentUserEmail() {
        return currentUserEmail;
    }

    public String getVerificationCode() {
        return verificationCode.getText().toString();
    }

    private void initUI() {
        verificationMessage = findViewById(R.id.VerifyActivity_verificationMessage);
        verificationCode = findViewById(R.id.VerifyActivity_verificationCode);
        confirmButton = findViewById(R.id.VerifyActivity_confirmButton);
        resendVerificationCodeText = findViewById(R.id.VerifyActivity_resendVerificationCodeText);
    }
}