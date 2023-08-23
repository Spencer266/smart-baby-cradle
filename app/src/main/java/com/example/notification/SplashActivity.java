package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.options.AuthSignUpOptions;
import com.amplifyframework.core.Amplify;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("CognitoPlugin", "Successful added plugin!");
        } catch (AmplifyException e) {
            Log.e("CognitoPlugin", "Error while adding plugin " + e.toString());
        }

        Handler handler = new Handler();
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                nextActivity();
            }
        }, 2000);
    }

    private void nextActivity() {
        Amplify.Auth.fetchAuthSession(
                result -> {
                    AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                    switch (cognitoAuthSession.getIdentityIdResult().getType()) {
                        case SUCCESS: {
                            Log.i(
                                    "IdentityFetch",
                                    "IdentityID: " + cognitoAuthSession.getIdentityIdResult().getValue()
                            );
                            Intent intent = new Intent(this, SignInAcitivity.class);
                            startActivity(intent);
                            break;
                        }

                        case FAILURE: {
                            Log.i(
                                    "IdentityFetch",
                                    "IdentityID not present because: " + cognitoAuthSession.getIdentityIdResult().getError().toString()
                            );
                            Intent intent = new Intent(this, MainActivity.class);
                            startActivity(intent);
                            break;
                        }

                    }
                },
                error -> {
                    Log.e(
                            "FetchAuthSession",
                            "Splash Activity nextActivity error:" + error.toString()
                    );
                    Intent intent = new Intent(this, MainActivity.class);
                    startActivity(intent);
                }
        );
    }
}