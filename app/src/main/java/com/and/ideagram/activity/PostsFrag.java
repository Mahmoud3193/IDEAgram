package com.and.ideagram.activity;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.and.ideagram.R;
import com.and.ideagram.adapters.AdapterHelper;
import com.and.ideagram.adapters.NewsFeedsAdapter;
import com.google.firebase.auth.FirebaseAuth;

/**
 * A simple {@link Fragment} subclass.
 */
public class PostsFrag extends Fragment {


    NewsFeedsAdapter adapter;
    AdapterHelper helper;
    RecyclerView rview;
    String mUId;
    SwipeRefreshLayout mSwipeRefreshLayout;


    public static PostsFrag newInstance(String uId) {
        PostsFrag frag = new PostsFrag();
        Bundle args = new Bundle();
        args.putString("uid", uId);
        frag.setArguments(args);
        return frag;
    }



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mUId = getArguments().getString("uid");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_posts, container, false);
        initializeActivity(view);
        return view;
    }


    public void initializeActivity(View view) {
        mSwipeRefreshLayout = view.findViewById(R.id.swipe_container2);
        helper = new AdapterHelper(getContext(), mSwipeRefreshLayout);
        adapter = helper.getAdapter(mUId);
        rview = view.findViewById(R.id.rview2);
        rview.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(getContext());
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        rview.setLayoutManager(llm);
    }

    @Override
    public void onStop() {
        super.onStop();
        helper.clearUserProfileData();
    }






}
