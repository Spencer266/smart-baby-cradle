package com.example.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.core.Amplify;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignInAcitivity extends AppCompatActivity {
    private TextView signUp, fogot_password;
    private EditText edt_email, edt_password;
    private Button btn_sign_in;

    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in_acitivity);
        initUI();
        userSignUp();
        userChangePassword();
    }

    private void userChangePassword() {
        fogot_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInAcitivity.this, FogotPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    private void userSignUp() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(SignInAcitivity.this, SignUp.class);
                startActivity(intent);
            }
        });

        btn_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                onClickSignIn();
            }
        });
    }

    private void onClickSignIn() {
        progressDialog.show();
        String email = edt_email.getText().toString().trim();
        String password = edt_password.getText().toString().trim();
        Amplify.Auth.signIn(
            email,
            password,
            result -> {
                if (result.isSignedIn()) {
                    Log.i("SignIn", "Sign in successfully with email: " + result);
                    progressDialog.dismiss();
                    // Sign in success, update UI with the signed-in user's information
                    Intent intent = new Intent(SignInAcitivity.this, MainActivity.class);
                    startActivity(intent);
                    finishAffinity();
                } else {
                    Log.i("SignIn", "Sign in unsuccessfully: " + result);
                    progressDialog.dismiss();
                    // If sign in fails, display a message to the user.

                    Toast.makeText(SignInAcitivity.this, "Đăng nhập thất bại. ",
                            Toast.LENGTH_SHORT).show();
                }
            },
            error -> {
                Log.e("SignIn", "Error while signing in: " + error);
            }
        );
    }

    private void initUI(){
        edt_email = findViewById(R.id.edt_email);
        edt_password = findViewById(R.id.edt_password);
        btn_sign_in = findViewById(R.id.btn_signIn);
        signUp = findViewById(R.id.signUp);
        fogot_password = findViewById(R.id.forgotPassword);
        progressDialog = new ProgressDialog(this);
    }

    //This is change from Mai Hoang Tung
}