package com.and.ideagram.adapters;

import android.content.Context;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;

import com.and.ideagram.R;
import com.and.ideagram.activity.InfoFrag;
import com.and.ideagram.activity.PostsFrag;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by file1 on 14/04/2018.
 */

public class FragAdptr extends FragmentPagerAdapter {

    private String mUId;

    public FragAdptr(FragmentManager fm, String uId) {
        super(fm);
        mUId = uId;
    }


    @Override
    public CharSequence getPageTitle(int position) {
        switch (position) {
            case 0: return  "Info";
            case 1: return  "Posts";
            default: return null;
        }
    }

    @Override
    public Fragment getItem(int position) {
        switch (position) {
            case 0: return InfoFrag.newInstance(mUId);
            case 1: return PostsFrag.newInstance(mUId);
            default: return null;
        }
    }

    @Override
    public int getCount() {
        return 2;
    }







}
