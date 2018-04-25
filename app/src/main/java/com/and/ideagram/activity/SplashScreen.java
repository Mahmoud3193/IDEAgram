package com.and.ideagram.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.and.ideagram.R;
import com.and.ideagram.data.FireAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.wang.avi.AVLoadingIndicatorView;

public class SplashScreen extends AppCompatActivity {

    final String TAG = "FLASH-SCREEN";
    private BroadcastReceiver broadcastReceiver;
    private AVLoadingIndicatorView avi;
    FireAuth fAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash_screen);
        initializeActivity();
        startAnim();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiverForFinish();
        fAuth.checkSignedin();
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
        stopAnim();
    }

    public void initializeActivity() {
        avi = findViewById(R.id.avi);
        fAuth = new FireAuth(SplashScreen.this) {
            @Override
            public void updateUI(FirebaseUser user) {
                if(user != null) startActivity(new Intent(SplashScreen.this, MainActivity.class).putExtra("UID", user.getUid()));
                else startActivity(new Intent(SplashScreen.this, LoginActivity.class));
            }
        };
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_splash_activity")) {
                    finish();
                }
            }
        };
    }

    public void registerReceiverForFinish (){
        registerReceiver(broadcastReceiver, new IntentFilter("finish_splash_activity"));
    }

    public String getFromPref(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(SplashScreen.this);
        return prefs.getString(key, null);
    }


    void startAnim(){
        avi.smoothToShow();
    }


    void stopAnim(){
        avi.smoothToHide();
    }
}
