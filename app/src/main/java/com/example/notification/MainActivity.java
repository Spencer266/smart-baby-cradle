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
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.amplifyframework.auth.cognito.AWSCognitoAuthSession;
import com.amplifyframework.core.Amplify;
import com.bumptech.glide.Glide;
import com.example.notification.fragment.Account;
import com.example.notification.fragment.ChangePassword;
import com.example.notification.fragment.History;
import com.example.notification.fragment.Home;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.messaging.FirebaseMessaging;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private DrawerLayout mdrawerLayout;
    private NavigationView navigationView;
    private static final int FRAGMENT_HOME = 0;
    private static final int FRAGMENT_HISTORY = 1;
    private static final int FRAGMENT_ACCOUNT = 2;
    private static final int FRAGMENT_CHANGE_PASSWORD = 3;
    private int currentFragment = FRAGMENT_HOME;

    private ImageView img_avatar;
    private TextView txt_name, txt_email;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initial();
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        mdrawerLayout = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, mdrawerLayout, toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);
        mdrawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        replaceFragment(new Home());
        navigationView.getMenu().findItem(R.id.nav_home).setChecked(true);
        showUserInformation();
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.nav_home) {
            if (currentFragment != FRAGMENT_HOME) {
                replaceFragment(new Home());
                currentFragment = FRAGMENT_HOME;
            }
        }
        else if (id == R.id.nav_history) {
            if (currentFragment != FRAGMENT_HISTORY) {
                replaceFragment(new History());
                currentFragment = FRAGMENT_HISTORY;
            }

        }
        else if(id == R.id.nav_account){
            if (currentFragment != FRAGMENT_ACCOUNT) {
                replaceFragment(new Account());
                currentFragment = FRAGMENT_ACCOUNT;
            }
        }
        else if(id == R.id.nav_change_password){
            if (currentFragment != FRAGMENT_CHANGE_PASSWORD) {
                replaceFragment(new ChangePassword());
                currentFragment = FRAGMENT_CHANGE_PASSWORD;
            }
        }
        else if(id == R.id.nav_signOut){
            FirebaseAuth.getInstance().signOut();
            Intent intent = new Intent(this, SignInAcitivity.class);
            startActivity(intent);
            finish();
        }

        mdrawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }

        @Override
        public void onBackPressed () {
            if (mdrawerLayout.isDrawerOpen(GravityCompat.START)) {
                mdrawerLayout.closeDrawer(GravityCompat.START);
            } else {
                super.onBackPressed();
            }
        }

        public void replaceFragment (Fragment fragment){
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            transaction.replace(R.id.content_frame, fragment);
            transaction.commit();
        }
        public void initial(){
            navigationView = findViewById(R.id.navigation_view);
            navigationView.setNavigationItemSelectedListener(this);
            img_avatar = navigationView.getHeaderView(0).findViewById(R.id.image_avatar);
            txt_name = navigationView.getHeaderView(0).findViewById(R.id.txt_name);
            txt_email = navigationView.getHeaderView(0).findViewById(R.id.txt_email);
        }

        public void showUserInformation(){
            Amplify.Auth.fetchAuthSession(
                    result -> {
                        AWSCognitoAuthSession cognitoAuthSession = (AWSCognitoAuthSession) result;
                        switch (cognitoAuthSession.getIdentityIdResult().getType()) {
                            case SUCCESS: {
                                Log.i(
                                        "getUserInformation",
                                        "Successfully retrieved data!"
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
    }
