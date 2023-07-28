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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUp extends AppCompatActivity {

    private EditText emailAddress, passwordAddress, rePasswordAddress;
    private Button signUp;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        initial();

        userSignup();
    }

    private void userSignup() {
        signUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                progressDialog.show();

                if(passwordAddress.getText().toString().equals(rePasswordAddress.getText().toString())){
                    String email = emailAddress.getText().toString().trim();
                    String password = passwordAddress.getText().toString().trim();
                    auth.createUserWithEmailAndPassword(email, password)
                            .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if (task.isSuccessful()) {
                                        progressDialog.dismiss();
                                        // Sign in success, update UI with the signed-in user's information
                                        Intent intent = new Intent(SignUp.this, MainActivity.class);
                                        startActivity(intent);
                                        finishAffinity();
                                    } else {
                                        progressDialog.dismiss();
                                        // If sign in fails, display a message to the user.

                                        Toast.makeText(SignUp.this, "Đăng ký thất bại. " + task.getException().getMessage(),
                                                Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }
                else{
                    Toast.makeText(SignUp.this, "Mật khẩu không khớp" , Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void initial() {
        emailAddress = findViewById(R.id.emailAddress);
        passwordAddress = findViewById(R.id.passwordTxt);
        rePasswordAddress = findViewById(R.id.rePasswordTxt);
        signUp = findViewById(R.id.takeSigup);
        progressDialog = new ProgressDialog(this);
    }
}