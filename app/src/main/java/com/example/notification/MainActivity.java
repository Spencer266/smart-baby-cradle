package com.example.notification;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.amplifyframework.auth.AuthUserAttributeKey;
import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.auth.cognito.result.AWSCognitoAuthSignOutResult;
import com.amplifyframework.auth.cognito.result.GlobalSignOutError;
import com.amplifyframework.auth.cognito.result.HostedUIError;
import com.amplifyframework.auth.cognito.result.RevokeTokenError;
import com.amplifyframework.core.Amplify;
import com.example.notification.fragment.DeviceManager;
import com.example.notification.fragment.PasswordManager;
import com.example.notification.fragment.History;
import com.example.notification.fragment.Home;
import com.google.android.material.navigation.NavigationView;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_HISTORY = 1;
    private static final int FRAGMENT_ADDING_DEVICE = 2;
    private static final int FRAGMENT_CHANGE_PASSWORD = 3;
    private int currentFragment = FRAGMENT_HOME;

    private ImageView userAvatar;
    private TextView username, userEmail;
    private static String userId;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("MainActivity", "Calling onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initUI();
        setupToolbar();

        navigationView.getMenu().findItem(R.id.MainActivity_nav_home).setChecked(true);
        showUserInformation();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        Log.i("MainActivity", "Calling onNavigationItemSelected");
        int id = item.getItemId();
        Log.i("MainActivity", "Choosing item: " + id);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                if (id == R.id.MainActivity_nav_home) {
                    if (currentFragment != FRAGMENT_HOME) {
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", getUserId());
                        Home nextFragment = new Home();
                        nextFragment.setArguments(bundle);
                        replaceFragment(nextFragment);
                        currentFragment = FRAGMENT_HOME;
                    }
                }
                else if (id == R.id.MainActivity_nav_adding_device) {
                    if (currentFragment != FRAGMENT_ADDING_DEVICE) {
                        Bundle bundle = new Bundle();
                        bundle.putString("userId", getUserId());
                        DeviceManager nextFragment = new DeviceManager();
                        nextFragment.setArguments(bundle);
                        replaceFragment(nextFragment);
                        currentFragment = FRAGMENT_ADDING_DEVICE;
                    }
                }
                else if (id == R.id.MainActivity_nav_history) {
                    if (currentFragment != FRAGMENT_HISTORY) {
                        replaceFragment(new History());
                        currentFragment = FRAGMENT_HISTORY;
                    }
                }
                else if(id == R.id.MainActivity_nav_change_password) {
                    if (currentFragment != FRAGMENT_CHANGE_PASSWORD) {
                        replaceFragment(new PasswordManager());
                        currentFragment = FRAGMENT_CHANGE_PASSWORD;
                    }
                }
            }
        });

        if(id == R.id.MainActivity_nav_signOut){
            signOut();

            Intent intent = new Intent(getApplicationContext(), SignInActivity.class);
            startActivity(intent);
            finish();
        }
        drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    @Override
    public void onBackPressed () {
        Log.i("MainActivity", "Calling onBackPressed");

        if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
            drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private void replaceFragment (Fragment fragment){
        Log.i("MainActivity", "Calling replaceFragment");

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.MainActivity_contentFrame, fragment);
        transaction.commit();
    }

    private void showUserInformation(){
        Log.i("MainActivity", "Calling showUserInformation");

        Amplify.Auth.fetchAuthSession(
            result -> {
                AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                switch (cognitoAuthSession.getIdentityIdResult().getType()) {
                    case SUCCESS: {
                        Log.i(
                            "getUserInformation",
                            "Successfully retrieved data!"
                        );

                        Amplify.Auth.getCurrentUser(
                                authUser -> {
                                    Log.i("getCurrentUser", authUser.getUsername());
                                    String currentUserEmail = authUser.getUsername(), currentUsername = "";
                                    userId = authUser.getUserId();
                                    for (int i = 0; i < currentUserEmail.length(); i++) {
                                        if (currentUserEmail.charAt(i) == '@') {
                                            break;
                                        }
                                        currentUsername += currentUserEmail.charAt(i);
                                    }
                                    username.setText(currentUsername);
                                    userEmail.setText(currentUserEmail);

                                    Bundle bundle = new Bundle();
                                    bundle.putString("userId", authUser.getUserId());
                                    Home nextFragment = new Home();
                                    nextFragment.setArguments(bundle);
                                    replaceFragment(nextFragment);
                                },
                                error -> Log.e("getCurrentUser", "Something is not right?", error)
                        );
                        break;
                    }
                    case FAILURE: {
                        Log.i(
                            "getUserInformation",
                            "Unsuccessfully retrieved data!"
                        );
                        break;
                    }
                }
            },
            error -> {
                Log.e(
                    "getUserInformation",
                    "Error while fetching userdata: " + error
                );
            }
        );
    }

    private void setupToolbar() {
        Log.i("MainActivity", "Calling setupToolbar");
        Toolbar toolbar = findViewById(R.id.MainActivity_toolbar);
        setSupportActionBar(toolbar);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
    }

    private void signOut() {
        Log.i("MainActivity", "Calling signOut");

        Amplify.Auth.signOut(signOutResult -> {
           if (signOutResult instanceof AWSCognitoAuthSignOutResult.CompleteSignOut) {
               Log.i("SignOut", "Successfully signed out!");
           } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.PartialSignOut) {
               Log.i("SignOut", "Partial signed out!");

               AWSCognitoAuthSignOutResult.PartialSignOut partialSignOutResult
                       = (AWSCognitoAuthSignOutResult.PartialSignOut) signOutResult;

               HostedUIError hostedUIError
                       = partialSignOutResult.getHostedUIError();

               if (hostedUIError != null) {
                   Log.e("SignOut", "HostedUIError: " + hostedUIError, hostedUIError.getException());
               }

               GlobalSignOutError globalSignOutError
                       = partialSignOutResult.getGlobalSignOutError();
               if (globalSignOutError != null) {
                   Log.e("SignOut", "GlobalSignOutError: " + globalSignOutError, globalSignOutError.getException());
               }

               RevokeTokenError revokeTokenError
                       = partialSignOutResult.getRevokeTokenError();
               if (revokeTokenError != null) {
                   Log.e("SignOut", "RevokeTokenError: " + revokeTokenError, revokeTokenError.getException());
               }

           } else if (signOutResult instanceof AWSCognitoAuthSignOutResult.FailedSignOut) {
                AWSCognitoAuthSignOutResult.FailedSignOut failedSignOut
                        = (AWSCognitoAuthSignOutResult.FailedSignOut) signOutResult;
                Log.e("SignOut", "FailedSignOut: " + failedSignOut, failedSignOut.getException());
           }
        });
    }

    public String getUserId() {
        return userId;
    }

    private void initUI(){
        Log.i("MainActivity", "Calling initial");

        navigationView = findViewById(R.id.MainActivity_navigationView);
        navigationView.setNavigationItemSelectedListener(this);
        userAvatar     = navigationView.getHeaderView(0).findViewById(R.id.MainActivity_NavigationView_userAvatar);
        username       = navigationView.getHeaderView(0).findViewById(R.id.MainActivity_NavigationView_username);
        userEmail      = navigationView.getHeaderView(0).findViewById(R.id.MainActivity_NavigationView_userEmail);
        drawerLayout   = findViewById(R.id.MainActivity_drawer_layout);
    }
}
