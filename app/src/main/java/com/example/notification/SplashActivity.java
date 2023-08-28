package com.example.notification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.amplifyframework.AmplifyException;
import com.amplifyframework.api.aws.AWSApiPlugin;
import com.amplifyframework.api.graphql.model.ModelMutation;
import com.amplifyframework.auth.cognito.AWSCognitoAuthPlugin;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.amplifyframework.datastore.AWSDataStorePlugin;
import com.amplifyframework.datastore.generated.model.Post;
import com.amplifyframework.datastore.generated.model.PostStatus;
import com.amplifyframework.datastore.generated.model.Todo;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("SplashActivity", "Calling onCreate");

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        addPlugin();

        Handler handler = new Handler();
        handler.postDelayed(this::nextActivity, 2000);
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
                        finish();
                        break;
                    }

                    case FAILURE: {
                        Log.i(
                            "IdentityFetch",
                            "IdentityID not present because: " + cognitoAuthSession.getIdentityIdResult().getError()
                        );
                        Intent intent = new Intent(this, SignInActivity.class);
                        startActivity(intent);
                        finish();
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
                finish();
            }
        );

        Log.i("SplashActivity", "Complete NextActivity");
    }

    private void addPlugin() {
        Log.i("SplashActivity", "Calling addPlugin");

        try {
            Amplify.addPlugin(new AWSCognitoAuthPlugin());

            Log.i("CognitoPlugin", "Successfully added Auth plugin!");
        } catch (AmplifyException e) {
            Log.e("CognitoPlugin", "Error while adding Auth plugin " + e);
        }

        try {
            Amplify.addPlugin(new AWSDataStorePlugin());

            Log.i("DataStorePlugin", "Successfully added DataStore plugin!");
        } catch (AmplifyException e) {
            Log.e("DataStorePlugin", "Error while adding DataStore plugin " + e);
        }

        try {
            Amplify.addPlugin(new AWSApiPlugin());

            Log.i("ApiPlugin", "Successfully added Api plugin!");
        } catch (AmplifyException e) {
            Log.e("ApiPlugin", "Error while adding Api plugin " + e);
        }

        try {
            Amplify.configure(getApplicationContext());

            Log.i("ConfigurePlugin", "Successfully configured plugins");
        } catch (AmplifyException e) {
            Log.e("ConfigurePlugin", "Error while configuring plugins " + e);
        }

        Todo todo = Todo.builder()
                .name("My first todo")
                .description("todo description")
                .build();

        Amplify.API.mutate(
                ModelMutation.create(todo),
                response -> Log.i("MyAmplifyApp", "Added Todo with id: " + response.getData().getId()),
                error -> Log.e("MyAmplifyApp", "Create failed", error)
        );
    }
}