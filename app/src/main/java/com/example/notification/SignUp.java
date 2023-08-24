package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;

public class SignUp extends AppCompatActivity {

    private EditText emailAddress, passwordAddress, rePasswordAddress;
    private Button signUp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SignUp", "Calling onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initial();

        userSignup();
    }

    private void userSignup() {
        Log.i("SignUp", "Calling userSignup");
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog.show();

                if(passwordAddress.getText().toString().equals(rePasswordAddress.getText().toString())){
                    String email = emailAddress.getText().toString().trim();
                    String password = passwordAddress.getText().toString().trim();

                    AuthSignUpOptions options = AuthSignUpOptions.builder()
                        .userAttribute(AuthUserAttributeKey.email(), email)
                        .build();
                    Amplify.Auth.signUp(email, password, options,
                        result -> {
                            Log.i("SignUp", "Sign Up successfully: " + result);
                            progressDialog.dismiss();
                            // Sign in success, update UI with the signed-in user's information
                            Intent intent = new Intent(SignUp.this, MainActivity.class);
                            startActivity(intent);
                            finishAffinity();
                        },
                        error -> {
                            Log.i("SignUp", "Sign Up Unsuccessfully: " + error);
                            progressDialog.dismiss();
                            // If sign in fails, display a message to the user.
//                            Toast.makeText(SignUp.this, "Đăng ký thất bại. " + error,
//                                    Toast.LENGTH_SHORT).show();
                        }
                    );
                }
                else {
                    Log.i("SignUp", "Sign Up Unsuccessfully: Passwords do not match!");
                    Toast.makeText(SignUp.this, "Mật khẩu không khớp" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initial() {
        Log.i("SignUp", "Calling initial");

        emailAddress = findViewById(R.id.emailAddress);
        passwordAddress = findViewById(R.id.passwordTxt);
        rePasswordAddress = findViewById(R.id.rePasswordTxt);
        signUp = findViewById(R.id.takeSigup);
        progressDialog = new ProgressDialog(this);
    }
}