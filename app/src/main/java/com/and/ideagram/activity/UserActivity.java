package com.and.ideagram.activity;

import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CollapsingToolbarLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;

import com.and.ideagram.R;
import com.and.ideagram.adapters.FragAdptr;

public class UserActivity extends AppCompatActivity {


    ViewPager viewPager;
    FragAdptr fragAdptr;
    Toolbar toolbar;
    TabLayout tabLayout;
    CollapsingToolbarLayout collapsingToolbarLayout;
    String uId;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user);
        initializeActivity();
        setAdptrs();
        toolbar.setTitle("ID : " + getIntent().getStringExtra("uid"));


    }

    public void initializeActivity() {
        uId = getIntent().getStringExtra("uid");
        toolbar = findViewById(R.id.toolbar4);
        fragAdptr = new FragAdptr(getSupportFragmentManager(), uId);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        collapsingToolbarLayout = findViewById(R.id.collapsing);
    }


    public void setAdptrs() {

        viewPager.setAdapter(fragAdptr);
        tabLayout.setupWithViewPager(viewPager);
    }





}
