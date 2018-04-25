package com.and.ideagram.activity;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.preference.PreferenceManager;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.and.ideagram.R;
import com.and.ideagram.adapters.AdapterHelper;
import com.and.ideagram.adapters.NewsFeedsAdapter;
import com.and.ideagram.data.FireAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MAINACTIVITY";

    Toolbar toolbar;
    RecyclerView rview;
    FireAuth fAuth;
    NewsFeedsAdapter adptr;
    AdapterHelper helper;
    SwipeRefreshLayout mSwipeRefreshLayout;
    CollapsingToolbarLayout ctl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        finishPreviousActivity();
        initializeActivity();

        Toast.makeText(this, "UID = " + fAuth.getCurrentUser().getUid(), Toast.LENGTH_LONG).show();
        setSupportActionBar(toolbar);
        ctl.setTitleEnabled(false);
        toolbar.setTitle("IDEAgram");

    }


    boolean checkConnection() {
        NetworkInfo activeNetwork  = ((ConnectivityManager) MainActivity.this.getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnectedOrConnecting();
        return isConnected;
    }

    public void initializeActivity() {

        ctl = findViewById(R.id.collaps);
        mSwipeRefreshLayout = findViewById(R.id.swipe_container);
        mSwipeRefreshLayout.setNestedScrollingEnabled(true);

        toolbar = findViewById(R.id.toolbar);
        fAuth = new FireAuth(MainActivity.this) {
            @Override
            public void updateUI(FirebaseUser user) {
                if(user == null) {
                    startActivity(new Intent(MainActivity.this, LoginActivity.class));
                    finish();
                }else{

                }
            }
        };
        rview = findViewById(R.id.rview);
        helper = new AdapterHelper(MainActivity.this, mSwipeRefreshLayout);
        adptr = helper.getAdapter();
        rview.setAdapter(adptr);
        LinearLayoutManager llm = new LinearLayoutManager(MainActivity.this);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rview.setLayoutManager(llm);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if(checkConnection()) {
        switch (item.getItemId()) {
            case R.id.add:
                startActivity(new Intent(MainActivity.this, PostActivity.class));
                break;
            case R.id.logout:
                new AlertDialog.Builder(MainActivity.this)
                        .setMessage("DO YOU REALLY WANT TO SIGNOUT?!!!")
                        .setPositiveButton("SURE", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                saveToPref("password", null);
                                fAuth.signout();

                            }
                        })
                        .setNegativeButton("OFCOURSE NOT", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        })
                        .create()
                        .show();
                break;
            case R.id.user:
                startActivity(new Intent(MainActivity.this, UserActivity.class).putExtra("uid", fAuth.getCurrentUser().getUid()));
                break;
            default:
                Toast.makeText(MainActivity.this, "UNKNOWN ACTION", Toast.LENGTH_LONG).show();
        }

        }else {Toast.makeText(MainActivity.this, "No Internet Connection", Toast.LENGTH_SHORT).show();}

        return super.onOptionsItemSelected(item);
    }

    public void finishPreviousActivity(){
        sendBroadcast(new Intent("finish_splash_activity"));
        sendBroadcast(new Intent("finish_login_activity"));
    }

    public void saveToPref(String key, String value) {
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(key, value);
        editor.apply();
    }








}
