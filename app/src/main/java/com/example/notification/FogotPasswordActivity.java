package com.example.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class FogotPasswordActivity extends AppCompatActivity {

    private TextView str_email;
    private Button btn_new_password;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fogot_password);
        uiInit();
        btn_new_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth auth = FirebaseAuth.getInstance();
                String emailAddress = str_email.getText().toString().trim();

                auth.sendPasswordResetEmail(emailAddress)
                        .addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()) {
                                    Toast.makeText(FogotPasswordActivity.this, "Send verify mail", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                                else{
                                    Toast.makeText(FogotPasswordActivity.this, "Email doesn't exist", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });
            }
        });
    }

    private void uiInit() {
        str_email = findViewById(R.id.str_email);
        btn_new_password = findViewById(R.id.btn_new_password);
    }
}