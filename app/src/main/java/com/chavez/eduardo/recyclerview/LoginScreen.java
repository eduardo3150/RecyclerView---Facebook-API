package com.chavez.eduardo.recyclerview;

import android.content.Intent;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.appevents.AppEventsLogger;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;

import java.util.Arrays;

public class LoginScreen extends AppCompatActivity {

    private LoginButton loginButton;
    private CallbackManager callbackManager;
    private AccessTokenTracker accessTokenTracker;
    private ProfileTracker profileTracker;

    private TextView info;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getApplicationContext());
        setContentView(R.layout.activity_login_screen);

        info = (TextView) findViewById(R.id.logInfo);
        callbackManager = CallbackManager.Factory.create();

        accessTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {

            }
        };

        profileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                goAhead(currentProfile);
            }
        };


        accessTokenTracker.startTracking();
        profileTracker.startTracking();


        loginButton = (LoginButton) findViewById(R.id.login_button);

        FacebookCallback<LoginResult> callback = new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                Profile profile = Profile.getCurrentProfile();
                goAhead(profile);
            }

            @Override
            public void onCancel() {
                info.setText("Login attempt cancelled");
            }

            @Override
            public void onError(FacebookException error) {
                info.setText("Login attempt failed " +error.getMessage());
            }
        };

        loginButton.setPublishPermissions(Arrays.asList("publish_actions"));
        loginButton.registerCallback(callbackManager,callback);


    }

    @Override
    protected void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        goAhead(profile);
    }


    @Override
    protected void onPause() {
        super.onPause();
    }


    @Override
    protected void onStop() {
        accessTokenTracker.stopTracking();
        profileTracker.stopTracking();
        super.onStop();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode,resultCode,data);
    }


    private void  goAhead(Profile profile){
        if (profile!=null){
        Intent intent = new Intent(LoginScreen.this,MainActivity.class);
        intent.putExtra("name",profile.getFirstName());
        intent.putExtra("surname",profile.getLastName());
        intent.putExtra("imageUrl",profile.getProfilePictureUri(200,200));
        startActivity(intent);
        }
    }




}
