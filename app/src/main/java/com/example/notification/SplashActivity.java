package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SplashActivity", "Calling onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());
            Amplify.configure(getApplicationContext());
            Log.i("CognitoPlugin", "Successful added plugin!");
        } catch (AmplifyException e) {
            Log.e("CognitoPlugin", "Error while adding plugin " + e);
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
        Log.i("SplashActivity", "Calling nextActivity");
        Amplify.Auth.fetchAuthSession(
            result -> {
                AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                switch (cognitoAuthSession.getIdentityIdResult().getType()) {
                    case SUCCESS: {
                        Log.i(
                            "IdentityFetch",
                            "IdentityID: " + cognitoAuthSession.getIdentityIdResult().getValue()
                        );
                        Intent intent = new Intent(this, MainActivity.class);
                        startActivity(intent);
                        break;
                    }

                    case FAILURE: {
                        Log.i(
                            "IdentityFetch",
                            "IdentityID not present because: " + cognitoAuthSession.getIdentityIdResult().getError()
                        );
                        Intent intent = new Intent(this, SignInActivity.class);
                        startActivity(intent);
                        break;
                    }

                }
            },
            error -> {
                Log.e(
                    "FetchAuthSession",
                    "Splash Activity nextActivity error:" + error
                );
                Intent intent = new Intent(this, MainActivity.class);
                startActivity(intent);
            }
        );
    }
}