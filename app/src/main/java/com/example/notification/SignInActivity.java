package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;

public class SignInActivity extends AppCompatActivity {
    private TextView signUp, forgotPassword;
    private EditText userEmail, userPassword;
    private Button signInButton;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SignInActivity", "Calling onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_acitivity);
        initUI();
        userSignUp();
        userChangePassword();
    }

    private void userChangePassword() {
        Log.i("SignInActivity", "Calling userChangePassword");

        forgotPassword.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, ForgotPasswordActivity.class);
            startActivity(intent);
            finish();
        });
    }

    private void userSignUp() {
        Log.i("SignInActivity", "Calling userSignUp");
        signUp.setOnClickListener(view -> {
            Intent intent = new Intent(SignInActivity.this, SignUp.class);
            startActivity(intent);
            finish();
        });

        signInButton.setOnClickListener(view -> onClickSignIn());
    }

    private void onClickSignIn() {
        Log.i("signInActivity", "Calling onClickSignIn");

        progressDialog.show();
        String email = userEmail.getText().toString().trim();
        String password = userPassword.getText().toString().trim();
        Amplify.Auth.signIn(
            email,
            password,
            result -> {
                if (result.isSignedIn()) {
                    Log.i("SignIn", "Sign in successfully with email: " + result);
                    progressDialog.dismiss();
                    // Sign in success, update UI with the signed-in user's information
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Log.i("SignIn", "Sign in unsuccessfully: " + result);
                    progressDialog.dismiss();
                    // If sign in fails, display a message to the user.

                    Toast.makeText(SignInActivity.this, "Đăng nhập thất bại. ",
                            Toast.LENGTH_SHORT).show();
                }
            },
            error -> Log.e("SignIn", "Error while signing in: " + error)

        );
    }

    private void initUI() {
        Log.i("SignInActivity", "Calling InitUI");

        userEmail = findViewById(R.id.SignInActivity_userEmail);
        userPassword = findViewById(R.id.SignInActivity_userPassword);
        signInButton = findViewById(R.id.SignInActivity_signInButton);
        signUp = findViewById(R.id.SignInActivity_signUp);
        forgotPassword = findViewById(R.id.SignInActivity_forgotPassword);
        progressDialog = new ProgressDialog(this);
    }
}