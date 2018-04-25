package com.and.ideagram.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.and.ideagram.R;
import com.and.ideagram.data.FireAuth;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.EmailAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private EditText mail;
    private EditText pass;
    private Button login;
    private TextView signup;
    FireAuth fAuth;
    private BroadcastReceiver broadcastReceiver;

    private static final String TAG = "From_Login_Activity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initializeActivity();
        setListeners();
        setTextToEditText(mail, getFromPref("email"));
        setTextToEditText(pass, getFromPref("password"));
        finishSplashActivity();
    }

    @Override
    protected void onStart() {
        super.onStart();
        registerReceiverForFinish();
    }

    public void initializeActivity() {
        mail = findViewById(R.id.email);
        pass = findViewById(R.id.pass);
        login = findViewById(R.id.loginbtn);
        fAuth = new FireAuth(LoginActivity.this) {
            @Override
            public void updateUI(FirebaseUser user) {
                if(user != null) {
                    saveToPref("email", getTextFromEditText(mail));
                    saveToPref("password", getTextFromEditText(pass));
                    startActivity(new Intent(LoginActivity.this, MainActivity.class).putExtra("UID", user.getUid()));
                }
            }
        };
        signup = findViewById(R.id.textView2);
        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context arg0, Intent intent) {
                String action = intent.getAction();
                if (action.equals("finish_login_activity")) {
                    finish();
                }
            }
        };
    }

    public void setTextToEditText(EditText et, String text) {
        if(text == null) return;
        et.setText(text);
    }

    public String getTextFromEditText(EditText et) {
        return et.getText().toString();
    }

    public void setListeners() {
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fAuth.signin(getTextFromEditText(mail), getTextFromEditText(pass));
            }
        });
        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, SignupActivity.class));
            }
        });
    }

    public void saveToPref(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }

    public String getFromPref(String key) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(LoginActivity.this);
        return prefs.getString(key, null);
    }

    public void registerReceiverForFinish (){
        registerReceiver(broadcastReceiver, new IntentFilter("finish_login_activity"));
    }

    @Override
    protected void onStop() {
        super.onStop();
        unregisterReceiver(broadcastReceiver);
    }

    public void finishSplashActivity() {
        sendBroadcast(new Intent("finish_splash_activity"));

    }

}

